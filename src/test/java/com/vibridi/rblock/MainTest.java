package com.vibridi.rblock;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

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
}
