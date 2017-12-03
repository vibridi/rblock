package com.vibridi.rblock.test;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import com.vibridi.rblock.RBlock;
import com.vibridi.rblock.core.BlockableRecord;
import com.vibridi.rblock.core.BlockingFunction;
import com.vibridi.rblock.core.BlockingPredicate;
import com.vibridi.rblock.core.Pair;
import com.vibridi.rblock.core.PredicateFactory;
import com.vibridi.rblock.helpers.IOUtils;
import com.vibridi.rblock.helpers.LangUtils;
import com.vibridi.rblock.helpers.PredicateDefinition;
import com.vibridi.rblock.predicate.CommonNGram;
import com.vibridi.rblock.predicate.CommonToken;
import com.vibridi.rblock.predicate.ExactMatch;
import com.vibridi.rblock.predicate.NCharPrefix;
import com.vibridi.rblock.predicate.NGramTFIDF;
import com.vibridi.rblock.predicate.OffByXInteger;
import com.vibridi.rblock.tfidf.TFIDFCosineDistance;

public class MainTest {

	@Test
    public void testApp() {
        Map<List<String>, String> map = new HashMap<>();
		
		map.put(Arrays.asList("1","2","3"), "mark");
		map.put(Arrays.asList("a","b","c"),	"saul");
		
		assertTrue(map.containsKey(Arrays.asList("1","2","3")));
		assertTrue(!map.containsKey(Arrays.asList("0","2","3")));
		assertTrue(map.get(Arrays.asList("1","2","3")).equals("mark"));
    }
	
	@Test
	public void testPairEquality() {
		Pair a = new Pair("string1","string2");
		Pair b = new Pair("string1","string2");
		Pair c = new Pair("string1","string2");
		Pair d = new Pair("abc","string2");
		Pair e = new Pair("string2","string1");
		
		
		assertTrue(a.equals(b) && b.equals(a));						// symmetry
		assertTrue(a.equals(a));									// reflexivity
		assertTrue(a.equals(b) && b.equals(c) && a.equals(c)); 		// transitivity
		assertTrue(a.equals(b) && a.hashCode() == b.hashCode()); 	// consistency
		
		assertTrue(!a.equals(d) && !b.equals(d));
		assertTrue(a.hashCode() != d.hashCode());
		
		assertTrue(a.equals(e) && a.hashCode() == e.hashCode());	// unordered
		
		Set<Pair> set = new HashSet<>();
		set.add(a);
		set.add(b);
		set.add(c);
		set.add(d);
		set.add(e);
		assertTrue(set.size() == 2);
		assertTrue(set.contains(a));
	}
	
	@Test
	public void testIndexingExactMatch() throws FileNotFoundException, IOException {
		List<Map<String, String>> records = IOUtils.readCSV("/employees-20.csv");
		BlockingFunction func = new BlockingFunction(new HashSet<>(Arrays.asList(new ExactMatch("ID", "LAST"))));
		func.block(records);
		
		Set<Pair> p = func.getUniquePairs();
		assertTrue(p.size() == 6);
		assertTrue(p.contains(new Pair("2","7a")));
		assertTrue(p.contains(new Pair("19","7a")));
		
		func = new BlockingFunction(new HashSet<>(Arrays.asList(
				new ExactMatch("ID", "LAST"),
				new ExactMatch("ID", "TITLE"))));
		func.block(records);
		
		Map<String, Set<Pair>> m = func.getAll();
		assertTrue(m.size() == 2);
		assertTrue(m.get("exactLAST").size() == 6);
		assertTrue(m.get("exactTITLE").size() == 3);
		assertTrue(func.getUniquePairs().size() == 8);
	}
	
	@Test
	public void testIndexingCommonToken() throws FileNotFoundException, IOException {
		List<Map<String, String>> records = IOUtils.readCSV("/employees-20.csv");
		BlockingFunction func = new BlockingFunction(new HashSet<>(Arrays.asList(
				new CommonToken("ID", "TITLE"))));
		
		func.block(records);
		
		Set<Pair> p = func.getUniquePairs();
		assertTrue(p.size() == 6);
		assertTrue(p.contains(new Pair("3","19")));
		assertTrue(p.contains(new Pair("2","3")));
	}
	
	@Test
	public void testOffByXInteger() throws FileNotFoundException, IOException {
		List<Map<String, String>> records = IOUtils.readCSV("/employees-20.csv");
		BlockingFunction func = new BlockingFunction(new HashSet<>(Arrays.asList(
				new OffByXInteger("ID", "SALARY", 2))));
		
		func.block(records);
		
		Set<Pair> p = func.getUniquePairs();
		
		assertTrue(p.size() == 4);
		assertTrue(p.contains(new Pair("6","9")));		// same number
		assertTrue(p.contains(new Pair("17","18")));	// off by 1
		assertTrue(p.contains(new Pair("16","18")));	// off by 2
		
		func = new BlockingFunction(new HashSet<>(Arrays.asList(
				new OffByXInteger("ID", "SALARY", 0))));
		
		func.block(records);
		
		p = func.getUniquePairs();
		
		assertTrue(p.size() == 1);
		assertTrue(p.contains(new Pair("6","9")));		// same number
	}
	
	@Test
	public void testOffByXIntegerOnNonNumbers() throws FileNotFoundException, IOException {
		List<Map<String, String>> records = IOUtils.readCSV("/employees-20.csv");
		BlockingFunction func = new BlockingFunction(new HashSet<>(Arrays.asList(
				new OffByXInteger("ID", "LAST", 2))));
		
		func.block(records);
		
		Set<Pair> p = func.getUniquePairs();
		
		assertTrue(p.size() == 0);
	}
	
	@Test
	public void testNgram() {
		String s = "new zealand";
		List<String> key = new ArrayList<>(Arrays.asList(
				"new", "ew ", "w z", " ze", "zea", "eal", "ala", "lan", "and"));
		List<String> grams = LangUtils.ngram(s, 3);
		assertTrue(key.containsAll(grams));
		assertTrue(key.removeAll(grams) && key.size() == 0);
		
		key = new ArrayList<>(Arrays.asList(
				"new ", "ew z", "w ze", " zea", "zeal", "eala", "alan", "land"));
		grams = LangUtils.ngram(s, 4);
		assertTrue(key.containsAll(grams));
		assertTrue(key.removeAll(grams) && key.size() == 0);
		
		s = "ne";
		key = new ArrayList<>(Arrays.asList("ne"));
		grams = LangUtils.ngram(s, 4);
		assertTrue(key.containsAll(grams));
		assertTrue(key.removeAll(grams) && key.size() == 0);
	}
	
	@Test
	public void testCommonNgram() throws FileNotFoundException, IOException {
		List<Map<String, String>> records = IOUtils.readCSV("/employees-5.csv");
		BlockingFunction func = new BlockingFunction(new HashSet<>(Arrays.asList(
				new CommonNGram("ID", "FIRST", 3),
				new CommonNGram("ID", "TITLE", 3))));
		
		func.block(records);
		
		Set<Pair> p = func.getUniquePairs();
		
		assertTrue(p.size() == 2);
		assertTrue(p.contains(new Pair("3","5")));		// on FIRST
		assertTrue(p.contains(new Pair("2","3")));		// on TITLE
	}
	
	@Test
	public void testTFIDF() {
		String s1 = "An apple a day keeps the doctor away";
		String s2 = "I normally eat an apple every day";

		double d = new TFIDFCosineDistance(s1,s2).calculate();
		assertTrue(d > 0.4 && d < 0.41);
		
		s1 = "An apple a day keeps the doctor away";
		s2 = "An apple a day keeps the doctor away";

		d = new TFIDFCosineDistance(s1,s2).calculate();
		assertTrue(d == 1.0);
		
		s1 = "An apple a day keeps the doctor away";
		s2 = "Totally unrelated document";

		d = new TFIDFCosineDistance(s1,s2).calculate();
		assertTrue(d == 0.0);
		
		List<String> l1 = LangUtils.ngram("lisa callaghan", 2);
		List<String> l2 = LangUtils.ngram("elisabeth armida callaghan", 2);
		
		d = new TFIDFCosineDistance(l1,l2).calculate();
		assertTrue(d > 0.72 && d < 0.73);
	}
	
	@Test
	public void testRecursiveInstantiation() throws Exception {
		List<PredicateDefinition> definitions = new ArrayList<>();
		List<Map<String, String>> records = IOUtils.readCSV("/employees-20.csv");
		List<String> fields = new ArrayList<>(records.get(0).keySet());
		
		Integer[] p1 = new Integer[] {2, 3, 4};
		Double[] p2 = new Double[] {2.0, 3.0, 4.0};
		Float[] p3 = new Float[] {0.4f, 0.5f, 0.6f};
		Integer[] p4 = new Integer[] {-1, 0, 2};
		definitions.add(PredicateDefinition.defineFor(fields, TestBlocker.class, p1, p2, p3, p4));
		Set<BlockingPredicate<?>> ps = PredicateFactory.instantiateAll("ID", definitions);
		
		assertTrue(ps.size() == p1.length * p2.length * p3.length * p4.length * (fields.size() - 1));
	}
	
	@Test
	public void testPredicateInstantiation() throws Exception {
		List<Map<String, String>> records = IOUtils.readCSV("/employees-20.csv");
		List<String> fields = new ArrayList<>(records.get(0).keySet());
		
		List<PredicateDefinition> definitions = new ArrayList<>();
		definitions.add(PredicateDefinition.defineFor(fields, ExactMatch.class, new Object[0]));
		definitions.add(PredicateDefinition.defineFor(fields, CommonToken.class, new Object[0]));
		definitions.add(PredicateDefinition.defineFor(fields, CommonNGram.class, new Integer[] {2,4,6}));
		definitions.add(PredicateDefinition.defineFor(fields, NCharPrefix.class, new Integer[] {3,5,7})); 
		definitions.add(PredicateDefinition.defineFor(fields, OffByXInteger.class, new Integer[] {0,1,10})); 
		definitions.add(PredicateDefinition.defineFor(fields, NGramTFIDF.class, new Integer[] {3,5}, new Double[] {0.2,0.4,0.6,0.8}));
		Set<BlockingPredicate<?>> ps = PredicateFactory.instantiateAll("ID", definitions);
		assertTrue(ps.size() == 95);		
	}
	
	@Test
	public void testSpringContextInstantiation() throws Exception {
		String path = MainTest.class.getResource("/applicationContext.xml").toString();
		Set<BlockingPredicate<?>> ps = PredicateFactory.instantiateAll(path);
		assertTrue(ps.size() == 95);
	}
	
	@Test
	public void testBlockableRecord() throws Exception {
		List<Map<String, String>> records = IOUtils.readCSV("/employees-5.csv");
		String idField = "ID";
		Set<BlockableRecord> set = records.stream().map(r -> new BlockableRecord(r, idField)).collect(Collectors.toSet()); 
		assertTrue(set.size() == records.size());	// checks uniqueness of the source records
		
		Map<String,String> map = new HashMap<>();
		map.put("ID", "5");
		map.put("LAST", "CAVALIGOS");
		map.put("FIRST", "NICHOLAS");
		map.put("MIDDLE", "C");
		map.put("TITLE", "CAPTAIN-EMT");
		map.put("SALARY", "116664");
		BlockableRecord test = new BlockableRecord(map, idField);
		assertTrue(set.contains(test));
		
		map.put("FIRST", "a different value");
		assertTrue(set.contains(test));
		
		BlockableRecord test2 = new BlockableRecord(map, idField);
		assertTrue(test.equals(test2));
		
		BlockableRecord test3 = new BlockableRecord(map, "FIRST");
		assertTrue(!test.equals(test3));
	}
	
	@Test(expected = IllegalStateException.class)
	public void testBlockableRecordCtorError() {
		Map<String,String> map = new HashMap<>();
		map.put("ID", "5");
		map.put("LAST", "CAVALIGOS");
		map.put("FIRST", "NICHOLAS");
		map.put("MIDDLE", "C");
		map.put("TITLE", "CAPTAIN-EMT");
		map.put("SALARY", "116664");
		BlockableRecord test = new BlockableRecord(map, "NON_EXISTANT_ID_FIELD");
		test.toString();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBlockableRecordPutError() {
		Map<String,String> map = new HashMap<>();
		map.put("ID", "5");
		map.put("LAST", "CAVALIGOS");
		map.put("FIRST", "NICHOLAS");
		map.put("MIDDLE", "C");
		map.put("TITLE", "CAPTAIN-EMT");
		map.put("SALARY", "116664");
		BlockableRecord test = new BlockableRecord(map, "ID");
		test.put("ID", "abcd");
	}
	
	@Test
	public void testLearn() throws Exception {
		List<Map<String, String>> records = IOUtils.readCSV("/employees-learn.csv");
		List<String> fields = new ArrayList<>(records.get(0).keySet());
		String id = "ID";
		
		List<PredicateDefinition> definitions = new ArrayList<>();
		definitions.add(PredicateDefinition.defineFor(fields, ExactMatch.class, new Object[0]));
		definitions.add(PredicateDefinition.defineFor(fields, CommonToken.class, new Object[0]));
		definitions.add(PredicateDefinition.defineFor(fields, CommonNGram.class, new Integer[] {2,4,6}));
		definitions.add(PredicateDefinition.defineFor(fields, NCharPrefix.class, new Integer[] {3,5,7})); 
		definitions.add(PredicateDefinition.defineFor(fields, OffByXInteger.class, new Integer[] {0,1,10})); 
		definitions.add(PredicateDefinition.defineFor(fields, NGramTFIDF.class, new Integer[] {3,5}, new Double[] {0.2,0.4,0.6,0.8}));
		Set<BlockingPredicate<?>> ps = PredicateFactory.instantiateAll("ID", definitions);
		
		Set<Pair> positivePairs = new HashSet<>();
		positivePairs.add(new Pair("1", "22"));
		positivePairs.add(new Pair("2", "19"));
		positivePairs.add(new Pair("12", "23"));
		positivePairs.add(new Pair("7a", "24"));
		positivePairs.add(new Pair("14", "25"));
		positivePairs.add(new Pair("17", "26"));
		positivePairs.add(new Pair("4", "27"));
		positivePairs.add(new Pair("8", "28"));
		positivePairs.add(new Pair("10", "16a"));
		positivePairs.add(new Pair("6", "29"));
		
		Set<Pair> negativePairs = new HashSet<>();
		for(int i = 0; i < records.size(); i++) {
			for(int j = i+1; j < records.size(); j++) {
				negativePairs.add(new Pair(records.get(i).get(id), records.get(j).get(id)));
			}
		}
				
		negativePairs.removeAll(positivePairs);
		BlockingFunction func = RBlock.learn(positivePairs, negativePairs, records, ps, id, 0, 100);
		Set<BlockingPredicate<?>> learned = func.getPredicates();
		assertTrue(learned.size() == 2);
		Set<String> names = learned.stream().map(BlockingPredicate::getName).collect(Collectors.toSet());
		assertTrue(names.contains("4gramFIRST"));
		assertTrue(names.contains("6gramTITLE"));
	}
		
}
