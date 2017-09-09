package com.vibridi.rblock;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
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
import com.vibridi.rblock.predicate.ExactMatch;

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
	public void testIndexing() throws FileNotFoundException, IOException {
		List<Map<String, String>> records = DataUtils.readCSV("/employees-20.csv");
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
	
	
}
