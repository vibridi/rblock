package com.vibridi.rblock.core;

import java.util.HashSet;
import java.util.Set;

public class TokenSet {

	private Set<String> set;
	
	public TokenSet(Set<String> set) {
		this.set = set;
	}
	
	@Override 
	public int hashCode() {
		return set.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof TokenSet) {
			Set<String> tmp = new HashSet<>(set);
			tmp.retainAll(((TokenSet) obj).set);
			return (tmp.size() == 1 && !tmp.contains("")) || tmp.size() > 0;
		}
		return false;
	}

}
