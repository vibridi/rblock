package com.vibridi.rblock;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.vibridi.rblock.core.BlockableRecord;
import com.vibridi.rblock.core.BlockingFunction;
import com.vibridi.rblock.core.BlockingPredicate;
import com.vibridi.rblock.core.Pair;
import com.vibridi.rblock.helpers.PredicateFacade;

public class RBlock {

	public static BlockingFunction learnDNF() {
		// TODO DNF algorithm
		return null;
	}
	
	/**
	 * Learns a disjunctive blocking function. 
	 * 
	 * @param positivePairs Set of coreferent pairs
	 * @param negativePairs Set of non-coreferent pairs
	 * @param records Record references
	 * @param predicates Instances of field-specific blocking predicates
	 * @param keyField Name of the primary key field
	 * @param e Maximum number of coreferent pairs allowed to remain uncovered
	 * @param h Maximum number of pairs any predicate may cover
	 * @return The learned blocking function
	 */
	public static BlockingFunction learn(Collection<Pair> positivePairs, Collection<Pair> negativePairs, Collection<Map<String,String>> records, 
			Collection<BlockingPredicate<?>> predicates, String keyField, final int e, final int h) {
		return learn(positivePairs, negativePairs, BlockableRecord.toMap(records, keyField), predicates, e, h);
	}
	
	/**
	 * Learns a disjunctive blocking function. 
	 * 
	 * @param positivePairs Set of coreferent pairs
	 * @param negativePairs Set of non-coreferent pairs
	 * @param mappedRecords Record references
	 * @param predicates Instances of field-specific blocking predicates
	 * @param e Maximum number of coreferent pairs allowed to remain uncovered
	 * @param h Maximum number of pairs any predicate may cover
	 * @return The learned blocking function
	 */
	public static BlockingFunction learn(Collection<Pair> positivePairs, Collection<Pair> negativePairs, 
			Map<String,BlockableRecord> mappedRecords, Collection<BlockingPredicate<?>> predicates, final int e, final int h) {
		
		Set<PredicateFacade> P = PredicateFacade.makeFacade(predicates);
		Set<Pair> B = new HashSet<>(positivePairs);
		Set<Pair> R = new HashSet<>(negativePairs);
		
		double t = (double) P.size(); // predicates under consideration
		double b = (double) B.size(); // number of coreferent pairs
		
		P.forEach(p -> {
			B.forEach(pos -> p.indexPair(pos, mappedRecords, true));
			R.forEach(neg -> p.indexPair(neg, mappedRecords, false));
		});
		
		// Discard all predicates for which cost ≥ h:
		P.removeIf(p -> p.getCost() >= h);
		
		// Check cover feasibility
		int maxCoveredPositives = P.stream().map(PredicateFacade::getCoveredPositives).collect(Collectors.toSet()).size();
		if(B.size() - maxCoveredPositives > e) {
			throw new IllegalStateException("Cover is not feasible. Either e or h are too low");
		}
		
		double g = Math.sqrt(t / Math.log(b));
		
		// Discard all negative pairs covered by more than g predicates
		Set<Pair> Rd = R.stream().filter(r -> degree(r,P) > g).collect(Collectors.toSet());
		P.forEach(p -> p.getCoveredNegatives().removeAll(Rd));
		
		// Weighted set cover
		Set<PredicateFacade> Tp = new HashSet<>();
		while(B.size() > e) {
			PredicateFacade ti = null;
			double ratio = 0.0;
			for(PredicateFacade pz : P) {
				double z = pz.getCoverage() / (1 + pz.getCost()); // cost is the weight
				if(z > ratio) {
					ratio = z;
					ti = pz;
				}
			}
			if(ratio == 0.0)
				break;
			Set<Pair> Tz = new HashSet<>(ti.getCoveredPositives());
			B.removeAll(Tz);
			P.remove(ti);
			P.forEach(p -> p.getCoveredPositives().removeAll(Tz));
			Tp.add(ti);
		}
		
		Set<BlockingPredicate<?>> learned = Tp.stream().map(PredicateFacade::getPredicate).collect(Collectors.toSet());
		learned.forEach(p -> p.clear());
		return new BlockingFunction(learned);
	}
	
	private static double degree(Pair negativePair, Set<PredicateFacade> P) {
		int d = 0;
		for(PredicateFacade p : P) {
			if(p.getCoveredNegatives().contains(negativePair))
				d++;
		}
		return (double) d;
	}

}
