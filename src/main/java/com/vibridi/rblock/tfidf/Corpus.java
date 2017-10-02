package com.vibridi.rblock.tfidf;
//package ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;


public class Corpus {
	
	private ArrayList<Document> documents;
	private HashMap<String, Set<Document>> invertedIndex;
	
	public Corpus(ArrayList<Document> documents) {
		this.documents = documents;
		invertedIndex = new HashMap<String, Set<Document>>();
		
		createInvertedIndex();
	}
	
	private void createInvertedIndex() {		
		for (Document document : documents) {
			Set<String> terms = document.getTermList();
			
			for (String term : terms) {
				if (invertedIndex.containsKey(term)) {
					Set<Document> list = invertedIndex.get(term);
					list.add(document);
				} else {
					Set<Document> list = new TreeSet<Document>();
					list.add(document);
					invertedIndex.put(term, list);
				}
			}
		}
	}
	
	public double getInverseDocumentFrequency(String term) {
		if (invertedIndex.containsKey(term)) {
			double size = documents.size();
			double documentFrequency = invertedIndex.get(term).size();
			return Math.log10(size / documentFrequency);
		} else {
			return 0;
		}
	}

	public ArrayList<Document> getDocuments() {
		return documents;
	}
	
	public Set<String> getVocabulary() {
		return invertedIndex.keySet();
	}
}