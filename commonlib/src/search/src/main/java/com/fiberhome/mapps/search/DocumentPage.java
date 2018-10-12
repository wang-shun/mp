package com.fiberhome.mapps.search;

import java.util.List;

import org.apache.lucene.document.Document;

public class DocumentPage {
	private int totalhits;
	
	private List<Document> docs;

	public int getTotalhits() {
		return totalhits;
	}

	public void setTotalhits(int totalhits) {
		this.totalhits = totalhits;
	}

	public List<Document> getDocs() {
		return docs;
	}

	public void setDocs(List<Document> docs) {
		this.docs = docs;
	}
	
	
}
