package com.vibridi.rblock.predicate;

import java.util.HashSet;
import java.util.Set;

import com.vibridi.rblock.core.BlockingPredicate;

public class ExactMatch extends BlockingPredicate<String> {
	
	public ExactMatch(String idName, String fieldName) {
		super(idName, fieldName);
	}

	@Override
	public Set<String> computeKey(String fieldValue) {
		Set<String> keys = new HashSet<>();
		if(fieldValue == null || fieldValue.length() == 0)
			return keys;
		keys.add(fieldValue);
		return keys;
	}

	@Override
	public String getName() {
		return "exact".concat(fieldName);
	}

	@Override
	public boolean isEmpty(String value) {
		return value.isEmpty();
	}

}
