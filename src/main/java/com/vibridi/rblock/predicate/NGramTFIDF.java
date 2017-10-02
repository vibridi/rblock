package com.vibridi.rblock.predicate;

import java.util.Collection;
import java.util.Collections;

import com.vibridi.rblock.core.BlockingPredicate;
import com.vibridi.rblock.helpers.LangUtils;
import com.vibridi.rblock.tfidf.TFIDFCosineDistance;

public class NGramTFIDF extends BlockingPredicate<String> {

	private int n;
	private double d;
	
	public NGramTFIDF(String idName, String fieldName, int n, double d) {
		super(idName, fieldName);
		this.n = n;
		this.d = d;	
	}
	
	@Override
	public Collection<String> computeKey(String fieldValue) {
		if(fieldValue == null || fieldValue.length() == 0)
			return Collections.emptyList();
		return LangUtils.ngram(fieldValue, n);
	}

	@Override
	public boolean isEmpty(String value) {
		throw new UnsupportedOperationException("N-gram TF-IDF cosine doesn't implement #isEmpty");
	}
	
	@Override
	public boolean equals(Collection<String> k1, Collection<String> k2) {		
		double dx = new TFIDFCosineDistance(k1,k2).calculate();
		return dx >= d;
	}

	@Override
	public String getName() {
		return Integer.toString(n).concat("gram").concat("tfidf").concat(Double.toString(d)).concat(fieldName);
	}
	


}
