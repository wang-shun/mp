package com.fiberhome.mapps.fileservice.impl.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fiberhome.mapps.fileservice.ServiceAccessor;
import com.fiberhome.mapps.fileservice.entity.FsFile;
import com.fiberhome.mapps.fileservice.response.RetriveFileInfo;
import com.fiberhome.mapps.fileservice.service.FileService;
import com.fiberhome.mapps.fileservice.service.FileServiceDownloadService;

public class FileSystemServiceAccessor implements ServiceAccessor {
	Logger LOG = LoggerFactory.getLogger(FileService.class);
	
	@Autowired
	FileServiceDownloadService fsds;

	@Value("${fileservice.path}")
	String storePath;

	@Override
	public String upload(InputStream is, String fileName, String contentType, String ext) throws IOException {
		long size = is.available();
		LOG.debug("上传的文件名为：" + fileName);
		// 获取文件的后缀名
		LOG.debug("上传的后缀名为：" + ext);
		// 文件上传后的路径
		String filePath = storePath;
		// 解决中文问题，liunx下中文路径，图片显示问题
		String fileId = UUID.randomUUID() + "";
		String finalName = fileId + "." + ext;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		String curdate = formatter.format(new Date());
		String finalPath = filePath + "/" + curdate + "/" + finalName;
		LOG.debug("目标文件名为：" + finalName);

		FsFile fsfile = new FsFile();
		fsfile.setId(fileId);
		fsfile.setFileName(fileName);
		fsfile.setContentType(contentType);
		fsfile.setContentSize(size);    
		fsfile.setFilePath(finalPath);

		File dest = new File(finalPath);
		// 检测是否存在目录
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}

		if (!dest.exists()) {
			dest.createNewFile();
		}
//		is.read
//		file.transferTo(dest);
		
		FileOutputStream fos = new FileOutputStream(dest);
		byte[] b = new byte[1024];
		int len = 0;
		while((len = is.read(b)) != -1){
			fos.write(b,0,len);
		}
		fos.close();
		
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
