package com.vibridi.rblock;

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

import org.junit.Test;

import com.vibridi.rblock.core.BlockingFunction;
import com.vibridi.rblock.core.Pair;
import com.vibridi.rblock.helpers.DataUtils;
import com.vibridi.rblock.helpers.IOUtils;
import com.vibridi.rblock.helpers.LangUtils;
import com.vibridi.rblock.predicate.CommonNGram;
import com.vibridi.rblock.predicate.CommonToken;
import com.vibridi.rblock.predicate.ExactMatch;
import com.vibridi.rblock.predicate.OffByXInteger;

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
		BlockingFunction func = new BlockingFunction(Arrays.asList(new ExactMatch("ID", "LAST")));
		func.block(records);
		
		Set<Pair> p = func.getUniquePairs();
		assertTrue(p.size() == 6);
		assertTrue(p.contains(new Pair("2","7a")));
		assertTrue(p.contains(new Pair("19","7a")));
		
		func = new BlockingFunction(Arrays.asList(
				new ExactMatch("ID", "LAST"),
				new ExactMatch("ID", "TITLE")));
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
		BlockingFunction func = new BlockingFunction(Arrays.asList(
				new CommonToken("ID", "TITLE")));
		
		func.block(records);
		
		Set<Pair> p = func.getUniquePairs();
		assertTrue(p.size() == 6);
		assertTrue(p.contains(new Pair("3","19")));
		assertTrue(p.contains(new Pair("2","3")));
	}
	
	@Test
	public void testOffByXInteger() throws FileNotFoundException, IOException {
		List<Map<String, String>> records = IOUtils.readCSV("/employees-20.csv");
		BlockingFunction func = new BlockingFunction(Arrays.asList(
				new OffByXInteger("ID", "SALARY", 2)));
		
		func.block(records);
		
		Set<Pair> p = func.getUniquePairs();
		
		assertTrue(p.size() == 4);
		assertTrue(p.contains(new Pair("6","9")));		// same number
		assertTrue(p.contains(new Pair("17","18")));	// off by 1
		assertTrue(p.contains(new Pair("16","18")));	// off by 2
		
		func = new BlockingFunction(Arrays.asList(
				new OffByXInteger("ID", "SALARY", 0)));
		
		func.block(records);
		
		p = func.getUniquePairs();
		
		assertTrue(p.size() == 1);
		assertTrue(p.contains(new Pair("6","9")));		// same number
	}
	
	@Test
	public void testOffByXIntegerOnNonNumbers() throws FileNotFoundException, IOException {
		List<Map<String, String>> records = IOUtils.readCSV("/employees-20.csv");
		BlockingFunction func = new BlockingFunction(Arrays.asList(
				new OffByXInteger("ID", "LAST", 2)));
		
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
		BlockingFunction func = new BlockingFunction(Arrays.asList(
				new CommonNGram("ID", "FIRST", 3),
				new CommonNGram("ID", "TITLE", 3)));
		
		func.block(records);
		
		Set<Pair> p = func.getUniquePairs();
		
		assertTrue(p.size() == 2);
		assertTrue(p.contains(new Pair("3","5")));		// on FIRST
		assertTrue(p.contains(new Pair("2","3")));		// on TITLE
	}
	
	@Test
	public void testTF() {
		String s1 = "The game of life is a game of everlasting learning";
		List<String> l1 = Arrays.asList(s1.split("[^\\w]"));
		Map<String,Double> tf = DataUtils.termFreq(l1);
		for(String t : l1) {
			if(t.equalsIgnoreCase("game") || t.equalsIgnoreCase("of"))
				assertTrue(tf.get(t) == 0.2);
			else
				assertTrue(tf.get(t) == 0.1);
		}
		
		s1 = "The unexamined life is not worth living";
		l1 = Arrays.asList(s1.split("[^\\w]"));
		tf = DataUtils.termFreq(l1);
		for(String t : l1) {
			assertTrue(tf.get(t) == (1.0 / 7.0));
		}
		
		s1 = "abb abb abb";
		l1 = Arrays.asList(s1.split("[^\\w]"));
		tf = DataUtils.termFreq(l1);
		assertTrue(tf.size() == 1);
		for(String t : l1) {
			assertTrue(tf.get(t) == 1.0);
		}
		
		s1 = "";
		l1 = Arrays.asList(s1.split("[^\\w]"));
		tf = DataUtils.termFreq(l1);
		assertTrue(tf.size() == 0);
	}
	
	@Test
	public void testIDF() {
		String s1 = "The game of life is a game of everlasting learning";
		String s2 = "The unexamined life is not worth living";
		
		List<String> l1 = Arrays.asList(s1.split("[^\\w]"));
		List<String> l2 = Arrays.asList(s2.split("[^\\w]"));
		
		Map<String,Double> idf = DataUtils.invDocFreq(l1, l2);
		System.out.println(idf.toString());
		
	}
	
}
