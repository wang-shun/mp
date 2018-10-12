package com.fiberhome.mapps.fileservice.request;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.rop.AbstractRopRequest;

public class VideoUploadRequest extends AbstractRopRequest {
	@NotNull
	private MultipartFile file;
	
	private int width = 640;
	
	private int height = 480;
	
	private int bitrate = 372_000;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getBitrate() {
		return bitrate;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}
	
	
}
