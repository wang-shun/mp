package com.fiberhome.mapps.search;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.queryparser.flexible.core.util.StringUtils;
import org.apache.lucene.util.BytesRef;

public abstract class AbstractDocumentBuilder implements DocumentBuilder<String> {

	@Override
	public Document build(String id) {
		Object result = getSourceAccessor().getEntity(id);

		Document doc = null;
		try {
			doc = buildDoc(result);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return doc;
	}

	@Override
	public List<Document> build(List<String> ids) {
		List<Document> docList = new ArrayList<Document>();
		List entities = getSourceAccessor().getEntities(ids);
		for (Object obj : entities) {
			try {
				docList.add(buildDoc(obj));
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return docList;
	}

	private Document buildDoc(Object result) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Document doc = new Document();

		List<FieldDefine> schema = this.getSchema();
		for (FieldDefine fd : schema) {
			String name = fd.getName();
			if (fd.isTs()) {
				long value = Long.parseLong(BeanUtils.getProperty(result, name));
				Field field = new LongField(name, value, Store.YES);
				doc.add(field);
				//要排序，必须添加一个同名的NumericDocValuesField
			    field = new NumericDocValuesField(name, value);
			    doc.add(field);
			    //要存储值，必须添加一个同名的StoredField
			    field = new StoredField(name, value);
			    doc.add(field);
			} else {
				String val = StringUtils.toString(BeanUtils.getProperty(result, name));
				if (val == null) val = "";
				Field field = new Field(name, val, fd.getType());
				doc.add(field);
				if (isSortField(name)) {
					doc.add(new SortedDocValuesField(name, new BytesRef(val.getBytes())));
				}
			}
		}
		return doc;
	}

	public List<Document> fetch(int limit, int offset) {
		List entities = getSourceAccessor().getEntities(limit, offset);
		if (entities == null || entities.size() == 0) {
			return null;
		}

		List<Document> docs = new ArrayList<Document>(limit);
		for (Object obj : entities) {
			try {
				docs.add(buildDoc(obj));
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			obj = null;
		}

		return docs;
	}

	public abstract List<FieldDefine> getSchema();

	public abstract SourceAccessor<String> getSourceAccessor();
	
	protected boolean isSortField(String field) {
		return false;
	}

}
