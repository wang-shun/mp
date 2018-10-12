package com.fiberhome.mapps.vote.entity;

public class MobilearkDocument {
	
	private String documentName;
	private String documentId;
	private String type;
	private String folderId;
	private String createTime;
	private String size;
	//文件摘要
	private String documentDesc;
	private String documentCreator;
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getDocumentDesc() {
		return documentDesc;
	}
	public void setDocumentDesc(String documentDesc) {
		this.documentDesc = documentDesc;
	}
	public String getDocumentCreator() {
		return documentCreator;
	}
	public void setDocumentCreator(String documentCreator) {
		this.documentCreator = documentCreator;
	}
	
	
}
