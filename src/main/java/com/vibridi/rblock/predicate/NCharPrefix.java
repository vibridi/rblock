package com.vibridi.rblock.predicate;

import java.util.HashSet;
import java.util.Set;

import com.vibridi.rblock.core.BlockingPredicate;

public class NCharPrefix extends BlockingPredicate<String> {
	
	private int n; 

	public NCharPrefix(String idName, String fieldName, Integer n) {
		super(idName, fieldName);
		this.n = n;
	}

	@Override
	public Set<String> computeKey(String fieldValue) {
		Set<String> keys = new HashSet<>();
		if(fieldValue == null || fieldValue.length() == 0)
			return keys;
		keys.add(fieldValue.substring(0, Math.min(n, fieldValue.length())));
		return keys;
	}

	@Override
	public boolean isEmpty(String value) {
		return value.isEmpty();
	}

	@Override
	public String getName() {
		return Integer.toString(n).concat("pref").concat(fieldName);
	}

}
