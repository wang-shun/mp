package com.fiberhome.mapps.search;

import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;

public class FieldDefine {
	private String name;
	private FieldType type;
	
	/**
	 * 是否为时间戳
	 */
	private boolean ts;
	
	public static FieldDefine tsField(String name) {
		FieldDefine fd = new FieldDefine(name);
		fd.setTs(true);
		return fd;
	}
	
	public static FieldDefine txtField(String name, boolean tokenized, boolean stored) {
		FieldType txtType = new FieldType();
		txtType.setStored(stored);
		txtType.setTokenized(tokenized);
		txtType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
		
		FieldDefine fd = new FieldDefine(name);
		fd.setType(txtType);
		return fd;
	}
	
	public FieldDefine(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}

	public boolean isTs() {
		return ts;
	}

	public void setTs(boolean ts) {
		this.ts = ts;
	}
	
	public boolean isTokenized() {
		return this.getType() != null && this.getType().tokenized();
	}
	
	public boolean isStored() {
		return this.getType() != null && this.getType().stored();
	}
}
