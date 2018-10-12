package com.fiberhome.mapps.fileservice;

import java.io.IOException;
import java.io.InputStream;

import com.fiberhome.mapps.fileservice.response.RetriveFileInfo;

public interface ServiceAccessor {
	public String upload(InputStream is, String fileName, String contentType, String ext) throws IOException;
	
	public RetriveFileInfo retrive(String filePath) throws Exception;
}
