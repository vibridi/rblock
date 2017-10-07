package com.vibridi.rblock.predicate;

import java.util.HashSet;
import java.util.Set;

import com.vibridi.rblock.core.BlockingPredicate;
import com.vibridi.rblock.helpers.LangUtils;

public class CommonNGram extends BlockingPredicate<String> {
	
	private final int n;
	
	public CommonNGram(String idName, String fieldName, Integer n) {
		super(idName, fieldName);
		this.n = n;
	}

	@Override
	public Set<String> computeKey(String fieldValue) {
		Set<String> keys = new HashSet<>();
		if(fieldValue == null || fieldValue.length() == 0)
			return keys;
		keys.addAll(LangUtils.ngram(fieldValue, n));
		return keys;
	}

	@Override
	public boolean isEmpty(String value) {
		return value.isEmpty();
	}

	@Override
	public String getName() {
		return Integer.toString(n).concat("gram").concat(fieldName);
	}


}
