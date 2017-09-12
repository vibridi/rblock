package com.vibridi.rblock.helpers;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

public class DataUtils {
	
	public static Map<String,Double> termFreq(Collection<String> document) {
		return document.stream()
			.filter(s -> s.length() > 0)
			.collect(groupingBy(Function.identity(), counting()))
			.entrySet()
			.stream()
			.collect(toMap(Entry::getKey, e -> (double) e.getValue() / (double) document.size()));
	}

	public static Map<String,Double> invDocFreq(Collection<String> doc1, Collection<String> doc2) {
		Map<String,Double> map = new HashMap<>();
		Set<String> set1 = new HashSet<>(doc1);
		Set<String> set2 = new HashSet<>(doc2);
		set1.forEach(s -> map.put(s, set2.remove(s) ? 1.0 : 1.0 + Math.log(2.0)));
		set2.forEach(s -> map.put(s, 1.0 + Math.log(2.0)));
		return map;
	}
	
	
	
}
