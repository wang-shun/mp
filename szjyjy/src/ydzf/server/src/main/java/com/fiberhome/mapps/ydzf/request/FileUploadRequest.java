package com.fiberhome.mapps.ydzf.request;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.rop.AbstractRopRequest;

public class FileUploadRequest extends AbstractRopRequest {
	@NotNull
	private MultipartFile file;
	
	private String directory;
	

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

}
