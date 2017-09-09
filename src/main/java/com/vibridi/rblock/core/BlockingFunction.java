package com.vibridi.rblock.core;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockingFunction {

	private List<BlockingPredicate> predicates;
	private Map<String,Set<Pair>> pairs;
	
	public BlockingFunction(List<BlockingPredicate> predicates) {
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

	public void save() {
		
	} // it will probably be useful to save the learned predicates somewhere
	
}
