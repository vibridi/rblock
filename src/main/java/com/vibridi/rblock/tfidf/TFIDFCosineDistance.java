package com.vibridi.rblock.tfidf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Main logic of this class results from a fork and debug of Hannah Cutler's repo:
 * https://github.com/hcutler/tf-idf
 * 
 */
public class TFIDFCosineDistance {

	private Document d1;
	private Document d2;
	private Corpus corpus;
	private HashMap<Document, HashMap<String, Double>> tfIdfWeights;
	
	public TFIDFCosineDistance(String s1, String s2) {
		this(Arrays.asList(s1.split("[^\\w]", -1)), Arrays.asList(s2.split("[^\\w]", -1)));
	}
	
	public TFIDFCosineDistance(Collection<String> c1, Collection<String> c2) {
		d1 = new Document(c1);
		d2 = new Document(c2);
		
		ArrayList<Document> documents = new ArrayList<Document>();
		documents.add(d1);
		documents.add(d2);
		corpus = new Corpus(documents);
		
		tfIdfWeights = new HashMap<>();
		Set<String> vocabulary = corpus.getVocabulary();
		
		for (Document document : corpus.getDocuments()) {
			HashMap<String, Double> weights = new HashMap<>();
			
			for (String term : vocabulary) {
				double tf = document.getTermFrequency(term);
				double idf = corpus.getInverseDocumentFrequency(term);				
				weights.put(term, tf * idf);
			}
			
			tfIdfWeights.put(document, weights);
		}
	}
	

	public double calculate() {
		double dot = dot(d1, d2);
		double m1 = magnitude(d1);
		double m2 = magnitude(d2);
		
		// Magnitude is 0 when all TF-IDF components are zero, since magnitude is a sum of squares.
		// TF-IDF is 0 when each term IDF is 0, and term IDF is 0 when the log argument is 1,
		// and finally the argument is 1 when corpus size / document frequency is 1,
		// which means that the term appears in all documents of the corpus.
		// Therefore all components of two TF-IDF vectors are 0 when the documents are exactly identical.
		if(m1 == m2 && m1 == 0.0) {
			return 1.0;
			
		} else if(m1 != 0.0 && m1 != 0.0) {
            return dot / (m1 * m2);
        
        } else {
            return 0.0;
        }
	}
	
	private double magnitude(Document document) {
		double magnitude = 0;
		HashMap<String, Double> weights = tfIdfWeights.get(document);

		for(double weight : weights.values()) {
			magnitude += Math.pow(weight,2);
		}
		
		return Math.sqrt(magnitude);
	}
	

	private double dot(Document d1, Document d2) {
		double product = 0;
		HashMap<String, Double> weights1 = tfIdfWeights.get(d1);
		HashMap<String, Double> weights2 = tfIdfWeights.get(d2);
		
		for(String term : weights1.keySet()) {
			product += weights1.get(term) * weights2.get(term);
		}
		
		return product;
	}
	
}
