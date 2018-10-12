package com.fiberhome.mapps.servicemanager.request;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.rop.AbstractRopRequest;

public class ConfigUploadRequest extends AbstractRopRequest {
	@NotNull
	private MultipartFile file;
	
	@NotNull
	private String application;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}
	
	
}
