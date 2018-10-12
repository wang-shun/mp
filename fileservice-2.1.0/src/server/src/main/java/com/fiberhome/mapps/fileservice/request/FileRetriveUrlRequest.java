package com.fiberhome.mapps.fileservice.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class FileRetriveUrlRequest extends AbstractRopRequest {
	@NotNull
	private String fileId;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

}
