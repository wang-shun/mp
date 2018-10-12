package com.fiberhome.mapps.vote.request;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.rop.AbstractRopRequest;

public class FileUploadRequest extends AbstractRopRequest {
	@NotNull
	private MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
