package com.vibridi.rblock.predicate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vibridi.rblock.core.BlockingPredicate;

public class ExactMatch extends BlockingPredicate {

	private Map<String,List<?>> index;
	
	public ExactMatch(String idName, String fieldName) {
		super(idName, fieldName);
	}

	@Override
	public Set<String> computeKey(String fieldValue) {
		Set<String> keys = Collections.emptySet();
		if(fieldValue == null || fieldValue.length() == 0)
			return keys;
		keys.add(fieldValue);
		return keys;
	}

}
