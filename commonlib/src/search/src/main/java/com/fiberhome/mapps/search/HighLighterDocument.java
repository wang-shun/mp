package com.fiberhome.mapps.search;

import org.apache.lucene.document.Document;

public class HighLighterDocument{
	private Document doc;
	private String hlStr;
	
	public HighLighterDocument(Document doc) {
		this(doc, "");
	}
	
	public HighLighterDocument(Document doc, String hlStr) {
		this.doc = doc;
		this.hlStr = hlStr;
	}
	
	public Document getDoc() {
		return doc;
	}
	public void setDoc(Document doc) {
		this.doc = doc;
	}
	public String getHlStr() {
		return hlStr;
	}
	public void setHlStr(String hlStr) {
		this.hlStr = hlStr;
	}
	
	
}
