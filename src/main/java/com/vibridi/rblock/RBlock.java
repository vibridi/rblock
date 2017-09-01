package com.vibridi.rblock;

import static java.util.stream.Collectors.*;

import java.util.Set;

import com.vibridi.rblock.core.BlockingPredicate;

public class RBlock {

	
	
	public static Set<BlockingPredicate<?>> learn(Set<?> B, Set<?> R, Set<BlockingPredicate<?>> P, int e, int h) {
		double t = (double) P.size();
		double b = (double) B.size();
		
		// 3
		double g = Math.sqrt(t / Math.log(b));
		
		// 4
		// discard all ri covered by more than g predicates
		// R = R.stream().filter( degree of ri in P <= g ).collect(toList());
		
		// 5
		
		// 7
		
		
		
		return null;
	}
	
	

}
