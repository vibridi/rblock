package com.vibridi.rblock.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BlockingPredicate<T> {
	
	protected final String fieldName;
	protected final String idName;
	protected Map<Collection<T>,List<String>> index;
	
	public BlockingPredicate(String idName, String fieldName) {
		this.idName = idName;
		this.fieldName = fieldName;
		index = new HashMap<>();
	}
	
	public abstract Collection<T> computeKey(String fieldValue);
	public abstract boolean isEmpty(T value);
	public abstract String getName();
	
	// assumes that all records are unique. the list in the index doesn't check for duplicates so if you had a malformed 
	// database you could make an inverted index with the same record appearing two times for a certain key.
	public Collection<T> index(Map<String,String> record) {
		Collection<T> kb = computeKey(record.get(fieldName));		
		if(kb.size() < 1 || (kb.size() == 1 && kb.contains("")))
			return kb;
		
		index.putIfAbsent(kb, new ArrayList<>()); 	// this compares hash codes
		
		index.keySet().forEach(ka -> { 				// this tests for predicate equality (i.e. intersection)
			if(equals(ka,kb))
				index.get(ka).add(record.get(idName));
		});
		
		return kb;
	}
	
//	public boolean equals(String x1, String x2) {
//		Set<String> s1 = computeKey(x1);
//		Set<String> s2 = computeKey(x2);
//		return equals(s1,s2);
//	}
	
	public boolean equals(Collection<T> k1, Collection<T> k2) {
		for(T s : k1) {
			if(!isEmpty(s) && k2.contains(s))
				return true;
		}
		return false;
	}
	
	public Set<Pair> getPairs() { // consider caching the value
		Set<Pair> pairs = index.values().stream() // TODO parallelize in case of many lists
			.filter(list -> list.size() > 1)
			.flatMap(list -> {
				List<Pair> tmp = new ArrayList<>();
				for(int i = 0; i < list.size(); i++) { // TODO parallelize in case of long lists
					for(int j = i+1; j < list.size(); j++) {
						tmp.add(new Pair(list.get(i), list.get(j)));
					}
				}
				return tmp.stream();
			})
			.collect(Collectors.toSet());
		// TODO consider clearing here
		return pairs;
	}
	
	public void clear() {
		index.clear();
	}

}
