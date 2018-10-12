package com.fiberhome.mapps.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;

public class SimpleDocumentBuilder extends AbstractDocumentBuilder {
	private List<FieldDefine> schema = new ArrayList<FieldDefine>();
	private SourceAccessor sourceAccessor;
	
	/**
	 * 添加索引字段
	 * @param name 字段名称，同来源实体属性名
	 * @param store 是否存储，设置存储选项则不对该字段进行分词
	 */
	public void addTextField(String name, boolean store) {
		FieldDefine fd = new FieldDefine(name);
		FieldType ft = new FieldType();
		ft.setStored(store);
		fd.setType(ft);
		
		schema.add(fd);
	}
	
	public void addTsField(String name) {
		FieldDefine fd = new FieldDefine(name);
		fd.setTs(true);
		
		schema.add(fd);
	}

	@Override
	public List<FieldDefine> getSchema() {
		return this.schema;
	}

	@Override
	public SourceAccessor getSourceAccessor() {
		return this.sourceAccessor;
	}
	
	public void setSourceAccessor(SourceAccessor sourceAccessor) {
		this.sourceAccessor = sourceAccessor;
	}

}
