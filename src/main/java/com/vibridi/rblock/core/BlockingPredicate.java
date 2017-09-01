package com.vibridi.rblock.core;

import java.util.Set;

public abstract class BlockingPredicate<T> {

	protected String fieldName;
	
	public BlockingPredicate(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public abstract Set<String> index(T fieldValue);
	
	public boolean equals(T x1, T x2) {
		Set<String> s1 = index(x1);
		Set<String> s2 = index(x2);
		s1.retainAll(s2);
		return s1.size() > 0;
	}
	

}
