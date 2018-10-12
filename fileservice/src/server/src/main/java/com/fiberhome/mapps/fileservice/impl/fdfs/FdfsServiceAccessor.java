package com.fiberhome.mapps.fileservice.impl.fdfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fiberhome.mapps.fileservice.ServiceAccessor;
import com.fiberhome.mapps.fileservice.exception.FilePathErrorException;
import com.fiberhome.mapps.fileservice.response.RetriveFileInfo;
import com.fiberhome.mapps.fileservice.service.FileService;
import com.github.tobato.fastdfs.domain.MateData;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

public class FdfsServiceAccessor implements ServiceAccessor {
	Logger LOG = LoggerFactory.getLogger(FileService.class);
	
	@Autowired
	FastFileStorageClient client;
	
	@Override
	public String upload(InputStream is, String fileName, String contentType, String ext, String md5)  throws IOException {
		long size = is.available();
		LOG.debug("Begin upload file: {}, size:{}", fileName, size);

		Set<MateData> metadatas = new HashSet<MateData>();
		MateData md = new MateData("fileName", fileName);
		metadatas.add(md);
		md = new MateData("size", size + "");
		metadatas.add(md);
		md = new MateData("contentType", contentType);
		metadatas.add(md);

		StorePath path = client.uploadFile(is, size, ext, metadatas);
		LOG.debug("Upload success, group: {}, path: {}", path.getGroup(), path.getPath());
		return path.getFullPath();
	}

	@Override
	public RetriveFileInfo retrive(String filePath) throws Exception {
		if (filePath.indexOf("/") < 0) {
			throw new FilePathErrorException();
		}
		
		RetriveFileInfo rfi = new RetriveFileInfo();

		StorePath path = null;
		path = StorePath.praseFromUrl(filePath);

		Set<MateData> metadatas = client.getMetadata(path.getGroup(), path.getPath());
		if (metadatas == null) {
			throw new FileNotFoundException();
		}
		for (MateData md : metadatas) {
			if ("size".equals(md.getName())) {
				//response.setSize(Long.parseLong(md.getValue()));
				rfi.setSize(Long.parseLong(md.getValue()));
			}

			if ("fileName".equals(md.getName())) {
				//response.setFileName(md.getValue());
				rfi.setFileName(md.getValue());
			}

			if ("contentType".equals(md.getName())) {
				//response.setContentType(md.getValue());
				rfi.setContentType(md.getValue());
			}
		}
		return rfi;
	}

	/*private StorePath upload(InputStream is, String fileName, String contentType, String ext) throws IOException {
		long size = is.available();
		LOG.debug("Begin upload file: {}, size:{}", fileName, size);

		Set<MateData> metadatas = new HashSet<MateData>();
		MateData md = new MateData("fileName", fileName);
		metadatas.add(md);
		md = new MateData("size", size + "");
		metadatas.add(md);
		md = new MateData("contentType", contentType);
		metadatas.add(md);

		StorePath path = client.uploadFile(is, size, ext, metadatas);
		LOG.debug("Upload success, group: {}, path: {}", path.getGroup(), path.getPath());
		return path;
	}*/
}
