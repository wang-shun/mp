package com.fiberhome.mapps.vote;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fiberhome.mapps.vote.service.FileService;

@Component
public class UpgradeTools {
	private static Logger LOG = LoggerFactory.getLogger(UpgradeTools.class);
	private static boolean scheduled = false;
	
	@Value("${vote.upgrade.migrate}")
    boolean upgradeMigrate = false;
    
    @Autowired
    JdbcTemplate template;
    
    @Scheduled(initialDelay = 1000, fixedDelay = 1000000)
    @Transactional(rollbackFor = {IOException.class, SQLException.class})
    public void upgradeFrom1_0() throws IOException {
    	
    	if (scheduled) return;
    	scheduled = true;
    	
    	if (!upgradeMigrate || checkUpgrade()) return;
		LOG.info("升级启动，执行中......");
    	// 迁移数据
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("db/upgrade.sql");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		while (true) {
			String sql = br.readLine();
			if (sql == null) {
				break;
			}
			if (sql.trim().length() == 0) {
				continue;
			}
			LOG.debug("execute: {}", sql);
			template.execute(sql);
		}
		
		// 更新图片
		updateImageIds("t_vote_info", "image");
		updateImageIds("t_vote_item", "image");
		
		// 设置upgrade状态
		confirmUpgrade();
    }

	private Boolean checkUpgrade() {
		Boolean ret = template.query("select upgraded from vt_vote_upgrade", new ResultSetExtractor<Boolean>() {
			@Override
			public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return "1".equals(rs.getString(1));
				}
				System.err.println("VT_VOTE_UPGRADE表无数据，未正确初始化。");
				return true;
			}
		});
		
		LOG.info("升级执行状态：{}", ret);
		return ret;
	}
	
	private void confirmUpgrade() {
		template.execute("update vt_vote_upgrade set upgraded = '1'");
	}
	
	private void updateImageIds(String table, String field) throws IOException {
		List<String> ids = getImageIds(table, field);
		for (String id : ids) {
			if (StringUtils.isNotEmpty(id)) {
				String newId = uploadToFileService(id);
				if (newId == null) {
					throw new IOException("upload failed");
				}
				
				// 更新原来的文件id
				LOG.debug("update image id from {} to {}", id, newId);
				updateImageId("v" + table, field, id, newId);
			}
		}
	}
	
	private List<String> getImageIds(String table, String field) {
		return template.query("select " + field + " from " + table, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});
	}
	
	private void updateImageId(String table, String field, String id, String newId) {
		String sql = "update " + table + " set " + field + "='" + newId + "' where " + field + "='" + id + "'";
		LOG.debug("update imageId: {}", sql);
		template.execute(sql);
	}
	
	@Autowired
	FileService fileService;
	
	private String uploadToFileService(String imageId) throws IOException {
		// 获取本地文件
		String fileInfo = getImageFileInfo(imageId);
		if (fileInfo == null || fileInfo.length() == 0) {
			return null;
		}
				
		// 上传到文件服务
		return  fileService.uploadLocalFile(new File(fileInfo));
	}
	
	private String getImageFileInfo(final String imageId) {
		return template.query("select photo_url, photo_server_filename from tf_sss_photo_upload where photo_upload_id='" + imageId + "'", new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getString(1) + "/" + rs.getString(2);
				}
				return null;
			}
		});
	}
}
