package com.fiberhome.mapps.fileservice.impl.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fiberhome.mapps.fileservice.ServiceAccessor;
import com.fiberhome.mapps.fileservice.entity.FsFile;
import com.fiberhome.mapps.fileservice.response.RetriveFileInfo;
import com.fiberhome.mapps.fileservice.service.FileService;
import com.fiberhome.mapps.fileservice.service.FileStorageService;
import com.fiberhome.mapps.fileservice.utils.IDGen;

public class FileSystemServiceAccessor implements ServiceAccessor {
	Logger LOG = LoggerFactory.getLogger(FileService.class);
	
	@Autowired
	FileStorageService fsds;

	@Value("${fileservice.path}")
	String storePath;

	@Override
	public String upload(InputStream is, String fileName, String contentType, String ext, String fileMD5) throws IOException {
		String existFileId = fsds.getFileByMD5(fileMD5);
		if(!"".equals(existFileId)){
			LOG.debug("检测到相同md5的文件,返回此文件id：" + existFileId);
			return existFileId;
		}
		long size = is.available();
		LOG.debug("上传的文件名为：" + fileName);
		// 获取文件的后缀名
		LOG.debug("上传的后缀名为：" + ext);
		// 文件上传后的路径
		String filePath = storePath;
		if(!filePath.endsWith("/")){
			filePath += "/";
		}
		// 解决中文问题，liunx下中文路径，图片显示问题
		String fileId = IDGen.shortId() + "." + ext.toLowerCase();
		String finalName = fileId;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String curdate = formatter.format(new Date());
		String branchPath = curdate + "/" + finalName;
		String finalPath = filePath + branchPath;
		LOG.debug("目标文件名为：" + finalName);

		FsFile fsfile = new FsFile();
		fsfile.setId(fileId);
		fsfile.setFileName(fileName);
		fsfile.setContentType(contentType);
		fsfile.setContentSize(size);    
		fsfile.setFilePath(branchPath);
		fsfile.setMd5(fileMD5);

		File dest = new File(finalPath);
		// 检测是否存在目录
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}

		if (!dest.exists()) {
			dest.createNewFile();
		}
		
		FileOutputStream fos = new FileOutputStream(dest);
		try {
			IOUtils.copy(is, fos);
		} finally {
			IOUtils.closeQuietly(fos);
		}
		
		fsds.insertFsFile(fsfile);
		
		return fileId;
	}

	@Override
	public RetriveFileInfo retrive(String fileId) throws IOException {
		FsFile fsfile = fsds.getFsFile(fileId);
		RetriveFileInfo rfi = new RetriveFileInfo();
		rfi.setSize(fsfile.getContentSize());
		rfi.setFileName(fsfile.getFileName());
		rfi.setContentType(fsfile.getContentType());
		return rfi;
	}

}
