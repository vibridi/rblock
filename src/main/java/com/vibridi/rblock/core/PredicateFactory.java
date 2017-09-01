package com.vibridi.rblock.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PredicateFactory {
	
	private Map<String,ClassLoader> genericPredicates;
	
	
	protected PredicateFactory() {
		genericPredicates = new HashMap<>();
		put("com.vibridi.rblock.predicate.ExactMatch", null);
	}
	
	public void put(String qualifiedName, ClassLoader loader) {
		genericPredicates.put(qualifiedName, loader);
	}

	
	public Set<BlockingPredicate<?>> instantiateAll(List<String> fields) throws Exception { 
		Set<BlockingPredicate<?>> set = new HashSet<>();
		
		for(String field : fields) {
			for(String clazz : genericPredicates.keySet()) {
				ClassLoader cl = genericPredicates.getOrDefault(clazz, this.getClass().getClassLoader());
				BlockingPredicate<?> pred = (BlockingPredicate<?>) cl.loadClass(clazz).getConstructor(String.class).newInstance(field);
				set.add(pred);
			}
		}
				
		return set;
	}
	
}
