package com.vibridi.rblock.helpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.vibridi.rblock.core.BlockableRecord;
import com.vibridi.rblock.core.BlockingPredicate;
import com.vibridi.rblock.core.Pair;

public class PredicateFacade {

	public static PredicateFacade makeFacade(BlockingPredicate<?> predicate) {
		return new PredicateFacade(predicate);
	}
	
	public static Set<PredicateFacade> makeFacade(Collection<BlockingPredicate<?>> predicates) {
		return predicates.stream().map(PredicateFacade::new).collect(Collectors.toSet());
	}
	
	private BlockingPredicate<?> predicate;
	private Set<Pair> coveredPositives;
	private Set<Pair> coveredNegatives;
	
	public PredicateFacade(BlockingPredicate<?> predicate) {
		this.predicate = predicate;
		this.coveredPositives = new HashSet<>();
		this.coveredNegatives = new HashSet<>();
	}
	
	public void indexPair(Pair pair, Map<String,BlockableRecord> records, boolean isCoreferent) {
		int a = predicate.index(records.get(pair.getFirst()).getData());
		int b = predicate.index(records.get(pair.getSecond()).getData());

		if(a == 1 && b > 1) { 
			if(isCoreferent) 
				coveredPositives.add(pair);
			else 
				coveredNegatives.add(pair);
		}

	}
	
	@Override
	public boolean equals(Object obj) {
		return predicate.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return predicate.hashCode();
	}
	
	public BlockingPredicate<?> getPredicate() {
		return predicate;
	}
	
	public int getCost() {
		return coveredNegatives.size();
	}

	public int getCoverage() {
		return coveredPositives.size();
	}

	public Set<Pair> getCoveredPositives() {
		return coveredPositives;
	}

	public Set<Pair> getCoveredNegatives() {
		return coveredNegatives;
	}
}
