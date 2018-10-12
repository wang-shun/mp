package com.fiberhome.mapps.fileservice.entity;

import java.sql.Date;

public class FsFile {
	private String id;
	private String fileName;
	private String contentType;
	private long contentSize;
	private String filePath;
	private Date uploadTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public long getContentSize() {
		return contentSize;
	}
	public void setContentSize(long contentSize) {
		this.contentSize = contentSize;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
}
