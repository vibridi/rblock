package com.vibridi.rblock.core;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

public class BlockingFunction {

	private List<BlockingPredicate> predicates;
	
	public BlockingFunction(List<BlockingPredicate> predicates) {
		this.predicates = predicates;
	}
	
	
	public void block(List<Map<String,String>> records) {
		records.forEach(r -> {
			predicates.forEach(p -> {
				p.index(r);
			});
		});
		
		// return something
		
	}

	
	
	/*
	 * 
	 * This is achieved by applying the indexing function for every blocking predicate or conjunction 
	 * in the learned blocking function to every record in the test dataset. 
	 * 
	 * Thus, an inverted index is constructed for each predicate or conjunction in the blocking function. 
	 * In each inverted index, every key is associated with a list of instances for which the indexing function 
	 * of the corresponding predicate returns the key value. 
	 * 
	 * index -> inverted index map[key, list[record]]
	 * 
	 * 
	 * Disjunctive and DNF blocking can then be performed by iterating over every key in all inverted indices 
	 * and returning all pairs of records that occur in the same list for any key.
	 * 
	 * 
	 */
	
	
}
