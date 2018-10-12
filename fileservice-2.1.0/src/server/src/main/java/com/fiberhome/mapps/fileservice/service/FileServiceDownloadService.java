package com.fiberhome.mapps.fileservice.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fiberhome.mapps.fileservice.entity.FsFile;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean
public class FileServiceDownloadService {
	Logger LOG = LoggerFactory.getLogger(FileServiceDownloadService.class);
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public void insertFsFile(final FsFile fsfile) {
		Date curdate = new Date();
		String sql = "insert into fs_file(id,file_name,content_type,content_size,file_path,upload_time) values(?,?,?,?,?,?)";
		Object[] obList = new Object[]{fsfile.getId(),fsfile.getFileName(),fsfile.getContentType(),fsfile.getContentSize(),fsfile.getFilePath(),curdate};
		LOG.debug("sql=====>"+sql);
		LOG.debug("params==>"+fsfile.getId()+","+fsfile.getFileName()+","+fsfile.getContentType()+","+fsfile.getContentSize()+","+fsfile.getFilePath()+","+curdate);
        jdbcTemplate.update(sql,obList);
    }
	
	public FsFile getFsFile(final String fileid){
        String sql = "select * from fs_file where id=?";
        LOG.debug("sql=====>"+sql);
        LOG.debug("params==>"+fileid);
        RowMapper<FsFile> rowMapper=new BeanPropertyRowMapper<FsFile>(FsFile.class);
        return (FsFile) jdbcTemplate.queryForObject(sql, new Object[]{fileid}, rowMapper);
	}
}
