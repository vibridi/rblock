package com.vibridi.rblock.predicate;

import java.util.Collections;
import java.util.Set;

import com.vibridi.rblock.core.BlockingPredicate;

public class ExactMatch extends BlockingPredicate<String> {

	public ExactMatch(String fieldName) {
		super(fieldName);
	}

	@Override
	public Set<String> index(String fieldValue) {
		Set<String> keys = Collections.emptySet();
		keys.add(fieldValue);
		return keys;
	}
}
