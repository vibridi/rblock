package com.vibridi.rblock.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BlockingPredicate {

	protected final String fieldName;
	protected final String idName;
	protected Map<TokenSet,List<String>> index;
	
	public BlockingPredicate(String idName, String fieldName) {
		this.idName = idName;
		this.fieldName = fieldName;
		index = new HashMap<>();
	}
	
	// assumes that all records are unique. the list in the index doesn't check for duplicates so if you had a malformed 
	// database you could make an inverted index with the same record appearing two times for a certain key.
	public Set<String> index(Map<String,String> record) {
		Set<String> keys = computeKey(record.get(fieldName));		
		if(keys.size() < 1 || (keys.size() == 1 && keys.contains("")))
			return keys;
		TokenSet key = new TokenSet(keys);
		index.computeIfAbsent(key, k -> new ArrayList<>()).add(record.get(idName));
		return keys;
	}
	
	public abstract Set<String> computeKey(String fieldValue);
	
	public boolean equals(String x1, String x2) {
		Set<String> s1 = computeKey(x1);
		Set<String> s2 = computeKey(x2);
		s1.retainAll(s2);
		return s1.size() > 0;
	}
	
	public void getPairs() {
		// TODO return all pairs in the inverted index
		index.values().stream() // TODO parallelize in case of many lists
			.filter(list -> list.size() > 1)
			.flatMap(list -> {
				for(int i = 0; i < list.size(); i++) { // TODO parallelize in case of long lists
					for(int j = i+1; j < list.size(); j++) {
						// make a pair
					}
				}
				return list.stream();
			})
			.collect(Collectors.toList());
	}
	
	public void clear() {
		index.clear();
	}

}
