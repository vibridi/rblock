package com.vibridi.rblock.core;

public class Pair {

	private final String first;
	private final String second;
	
	public Pair(String first, String second) { // change name to ImmutablePair?
		this.first = first == null ? "" : first;
		this.second = second == null ? "" : second;
	}
	
	public Pair(Pair that) {
		this.first = that.first;
		this.second = that.second;
	}

	public String getFirst() {
		return first;
	}

	public String getSecond() {
		return second;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(!(obj instanceof Pair))
			return false;
		Pair that = (Pair) obj;
		return (this.first.equals(that.first) && this.second.equals(that.second))
				|| (this.first.equals(that.second) && this.second.equals(that.first));
	}

	@Override
	public int hashCode() {
		int hash = (61 * first.hashCode()) ^ (61 * second.hashCode());
		return hash * 17;
	}

	@Override
	public String toString() {
		return first.concat(",").concat(second);
	}
	
}
