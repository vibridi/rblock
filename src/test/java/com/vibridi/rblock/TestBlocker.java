package com.vibridi.rblock;

import java.util.Collection;

import com.vibridi.rblock.core.BlockingPredicate;

public class TestBlocker extends BlockingPredicate<String> {

	int x, b;
	double d;
	float f;

	public TestBlocker(String idName, String fieldName, Integer x, Double d, Float f, Integer b) {
		super(idName, fieldName);
		this.x = x;
		this.d = d;
		this.f = f;
		this.b = b;
	}

	@Override
	public Collection<String> computeKey(String fieldValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
