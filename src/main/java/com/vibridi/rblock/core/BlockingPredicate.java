package com.vibridi.rblock.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BlockingPredicate<T> {
	
	public enum Predicate {
		EXACT_MATCH,
		N_CHAR_PREFIX,
		COMMON_TOKEN,
		COMMON_N_GRAM,
		N_GRAM_TFIDF,
		OFF_BY_X_INTEGER
	}
	
	protected final String fieldName;
	protected final String idName;
	protected Map<Collection<T>,List<String>> index;
	
	public BlockingPredicate(String idName, String fieldName) {
		this.idName = idName;
		this.fieldName = fieldName;
		index = new HashMap<>();
	}
	
	/**
	 * Computes the key used in the inverted index.
	 * @param fieldValue Field value from which the key is computed
	 * @return The key as a collection of objects. Must be not null.
	 */
	public abstract Collection<T> computeKey(String fieldValue);
	public abstract boolean isEmpty(T value);
	public abstract String getName();
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override 
	public boolean equals(Object obj) {
		if(obj instanceof BlockingPredicate) {
			BlockingPredicate<?> that = (BlockingPredicate<?>) obj;
			return this.getName().equals(that.getName());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}
	
	
	/**
	 * Adds the record to an inverted index, where the key is the output of the predicate.
	 * Assumes that all records are unique based on a master key. The list in the index doesn't check for duplicates 
	 * so if you had a malformed database you could index the same record with itself.
	 * @param record Record represented as a field-value map
	 * @return 
	 * 		<ul>
	 * 			<li>0: the record wasn't indexed</li>
	 * 			<li>1: the record was indexed with a new key</li>
	 * 			<li>2+: the record was indexed and paired</li>
	 * 		</ul>
	 */
	public int index(Map<String,String> record) {
		int ret = 0;
		Collection<T> kb = computeKey(record.get(fieldName));		
		if(kb.size() < 1 || (kb.size() == 1 && kb.contains("")))
			return ret;
		
		index.putIfAbsent(kb, new ArrayList<>()); 	// this compares hash codes
		
		for(Collection<T> ka : index.keySet()) {	// this tests for predicate equality (i.e. intersection)
			if(keysEqual(ka,kb)) {
				index.get(ka).add(record.get(idName));	
				ret = Math.max(ret, index.get(ka).size());
			}
		}
		
		return ret;
	}
	
	public boolean keysEqual(Collection<T> k1, Collection<T> k2) {
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
