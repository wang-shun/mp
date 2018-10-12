package com.fiberhome.mos.core.openapi.rop.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

public class MulitipartFileResourceWrapper extends FileSystemResource {
	private MultipartFile fileItem;

	public MulitipartFileResourceWrapper(String path) {
		super(path);
	}
	
	public MulitipartFileResourceWrapper(MultipartFile fileItem) {
		this(fileItem.getOriginalFilename());
		this.fileItem = fileItem;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return fileItem.getInputStream();
	}

	@Override
	public boolean isWritable() {
		return false;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		throw new IllegalAccessError("not support method");
	}

	@Override
	public URL getURL() throws IOException {
		return super.getURL();
	}

	@Override
	public URI getURI() throws IOException {
		return super.getURI();
	}
	
	@Override
	public File getFile() {
		throw new IllegalAccessError("not support method");
	}

	@Override
	public long contentLength() throws IOException {
		return fileItem.getSize();
	}

	@Override
	public String getFilename() {
		return fileItem.getOriginalFilename();
	}
	
	
}
