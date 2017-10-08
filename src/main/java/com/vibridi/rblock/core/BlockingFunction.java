package com.vibridi.rblock.core;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlockingFunction {

	private Set<BlockingPredicate<?>> predicates;
	private Map<String,Set<Pair>> pairs;
	
	/**
	 * Forces the input predicates to be unique
	 * @param predicates
	 */
	public BlockingFunction(Set<BlockingPredicate<?>> predicates) {
		this.predicates = predicates;
		this.pairs = null;
	}
	
	public void block(List<Map<String,String>> records) {
		records.forEach(r -> {
			predicates.forEach(p -> {
				p.index(r);
			});
		});
	}
	
	public Map<String,Set<Pair>> getAll() {
		if(pairs == null)
			pairs = predicates.stream().collect(toMap(BlockingPredicate::getName, BlockingPredicate::getPairs));
		return pairs;
	}
	
	public Set<Pair> getUniquePairs() {
		if(pairs == null)
			getAll();
		return pairs.values().stream().flatMap(s -> s.stream()).collect(toSet());
	}
	
	public Set<BlockingPredicate<?>> getPredicates() {
		return predicates;
	}

	public void save() {
		// TODO it will probably be useful to save the learned predicates somewhere
	}
	
}
