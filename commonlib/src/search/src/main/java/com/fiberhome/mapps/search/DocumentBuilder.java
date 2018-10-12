package com.fiberhome.mapps.search;

import java.util.List;

import org.apache.lucene.document.Document;

public interface DocumentBuilder<T> {
	List<FieldDefine> getSchema();
	Document build(T id);
	List<Document> build(List<T> ids);
	
	List<Document> fetch(int limit, int offset);
}
