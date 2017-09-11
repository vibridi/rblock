package com.vibridi.rblock.predicate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.vibridi.rblock.core.BlockingPredicate;

public class CommonToken extends BlockingPredicate<String> {

	public CommonToken(String idName, String fieldName) {
		super(idName, fieldName);
	}
	
	@Override
	public Set<String> computeKey(String fieldValue) {
		Set<String> keys = new HashSet<>();
		if(fieldValue == null || fieldValue.length() == 0)
			return keys;
		keys.addAll(Arrays.asList(fieldValue.split(" ", -1)));
		return keys;
	}

	@Override
	public String getName() {
		return "cmtok".concat(fieldName);
	}

	@Override
	public boolean isEmpty(String value) {
		return value.length() <= 0;
	}

}
