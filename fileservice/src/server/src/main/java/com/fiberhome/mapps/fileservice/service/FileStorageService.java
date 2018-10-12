package com.fiberhome.mapps.fileservice.service;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fiberhome.mapps.fileservice.entity.FsFile;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean
public class FileStorageService {
	Logger LOG = LoggerFactory.getLogger(FileStorageService.class);
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Value("${fileservice.path}")
	String storePath;
	
	public void insertFsFile(final FsFile fsfile) {
		Date curdate = new Date();
		String sql = "insert into FS_FILE(id,file_name,content_type,content_size,file_path,upload_time,md5) values(?,?,?,?,?,?,?)";
		Object[] obList = new Object[]{fsfile.getId(),fsfile.getFileName(),fsfile.getContentType(),fsfile.getContentSize(),fsfile.getFilePath(),curdate,fsfile.getMd5()};
		LOG.debug("sql=====>"+sql);
		LOG.debug("params==>"+fsfile.getId()+","+fsfile.getFileName()+","+fsfile.getContentType()+","+fsfile.getContentSize()+","+fsfile.getFilePath()+","+curdate+","+fsfile.getMd5());
        jdbcTemplate.update(sql,obList);
    }
	
	public FsFile getFsFile(final String fileid){
        String sql = "select * from FS_FILE where id=?";
        LOG.debug("sql=====>"+sql);
        LOG.debug("params==>"+fileid);
        RowMapper<FsFile> rowMapper=new BeanPropertyRowMapper<FsFile>(FsFile.class);
        return (FsFile) jdbcTemplate.queryForObject(sql, new Object[]{fileid}, rowMapper);
	}
	
	public String getFileByMD5(final String MD5){
		String sql = "SELECT COUNT(*) FROM FS_FILE WHERE MD5 = ?";
		LOG.debug("sql=======>"+sql);
		LOG.debug("params====>"+MD5);
		int existNum = (int) jdbcTemplate.queryForObject( sql, new Object[] {MD5}, int.class);
		LOG.debug("existNum==>"+existNum);
		if(existNum != 0){
			sql = "SELECT ID FROM FS_FILE WHERE MD5 = ?";
			LOG.debug("sql=======>"+sql);
			LOG.debug("params====>"+MD5);
			String fileId = (String) jdbcTemplate.queryForObject( sql, new Object[] {MD5}, java.lang.String.class);
			return fileId;
		}else{
			return "";
		}
	}
	
	public File getFile(String filePath) {
		File file = null;
		if(!storePath.endsWith("/")){
			storePath += "/";
		}
		if(filePath.startsWith("upload/")){
			file = new File(filePath);
			if(!file.exists()){
				filePath = storePath + filePath;
				file = new File(filePath);
			}
		}else{
			String tempPath = storePath + filePath;
			file = new File(tempPath);
			if(!file.exists()){
				file = new File(filePath);
			}
		}
		return file;
	}
}
