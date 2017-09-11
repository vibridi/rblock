package com.vibridi.rblock.predicate;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vibridi.rblock.core.BlockingPredicate;
import com.vibridi.rblock.helpers.LangUtils;

public class OffByXInteger extends BlockingPredicate<Integer> {

	private final int x;
	
	public OffByXInteger(String idName, String fieldName, int x) {
		super(idName, fieldName);
		this.x = Math.abs(x);
	}

	@Override
	public Set<Integer> computeKey(String fieldValue) {
		Set<Integer> keys = new HashSet<>();
		if(fieldValue == null || fieldValue.length() == 0)
			return keys;
		keys.addAll(Arrays.stream(fieldValue.split(" ", -1))
				.filter(s -> LangUtils.isNumeric(s))
				.map(Integer::parseInt)
				.collect(toList()));
		return keys;
	}

	@Override
	public boolean equals(Set<Integer> k1, Set<Integer> k2) {
		Set<Integer> kmin = new HashSet<>(k1.size() < k2.size() ? k1 : k2);
		List<Integer> kmax = new ArrayList<>(kmin == k1 ? k2 : k1);
		Collections.sort(kmax);
		for(Integer x1 : kmin) {
			if(kmax.contains(x1) || (kmax.get(0) >= x1.intValue() - x) && (kmax.get(kmax.size()-1) <= x1.intValue() + x))
				return true;
		}
		return false;
	}
	
	@Override
	public String getName() {
		return "offx".concat(fieldName);
	}

	@Override
	public boolean isEmpty(Integer value) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
