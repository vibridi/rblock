package com.vibridi.rblock.tfidf;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * This class represents one document.
 * It will keep track of the term frequencies.
 * @author swapneel
 *
 */
public class Document implements Comparable<Document> {

	private HashMap<String, Integer> termFrequency;
	private String[] terms;

	public Document(String text) {
		this(text.split("[^\\w]", -1));
	}
	
	public Document(Collection<String> collection) {
		this(collection.toArray(new String[0]));
	}
	
	public Document(String[] terms) {
		this.terms = terms;
		termFrequency = new HashMap<String, Integer>();
		preprocess();
	}
	
	private void preprocess() {
		for(String nextWord : terms) {
			if (!nextWord.isEmpty())
				termFrequency.compute(nextWord.toLowerCase(), (o,n) -> { return n == null ? 1 : n+1; });
		}

	}

	public double getTermFrequency(String word) {
		if (termFrequency.containsKey(word)) {
			return termFrequency.get(word);
		} else {
			return 0;
		}
	}

	public Set<String> getTermList() {
		return termFrequency.keySet();
	}

	@Override
	public int compareTo(Document that) {
		return equals(that) ? 1 : 0;
	}
	
	@Override
	public int hashCode() {
		return terms.hashCode();
	}
	
	@Override 
	public boolean equals(Object obj) {
		if(!(obj instanceof Document))
			return false;
		Document that = (Document) obj;
		return Arrays.equals(this.terms, that.terms);
	}

	@Override
	public String toString() {
		return Stream.of(terms)
			.collect(Collectors.joining(" "));
	}
}