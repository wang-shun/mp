package com.fiberhome.mapps.sign;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.fiberhome.mapps.contact.pojo.MyDepartment;
import com.fiberhome.mapps.contact.service.MplusAccessService;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.sign.service.FileService;

import net.sf.json.*;

@Component
public class UpgradeTools {
	private static Logger LOG = LoggerFactory.getLogger(UpgradeTools.class);
	private static boolean scheduled = false;
	
	@Value("${sign.upgrade.migrate}")
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
		
		//更新dep_order
		updateDepOrders("t_sign");
		LOG.info("===数据库复制完成===");
		
		
		// 更新图片
		updateImageIds("t_sign_image", "image");
		LOG.info("===本地图片上传完成===");
		
		// 设置upgrade状态
		confirmUpgrade();
		LOG.info("===迁移完成===");
    }
    
    private String getOrgId(String table) {
		return template.query("SELECT DISTINCT org_id FROM "+table, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getString(1);
				}
				return null;
			}
		});
	}

	private Boolean checkUpgrade() {
		Boolean ret = template.query("select upgraded from sign_upgrade", new ResultSetExtractor<Boolean>() {
			@Override
			public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return "1".equals(rs.getString(1));
				}
				System.err.println("SIGN_UPGRADE表无数据，未正确初始化。");
				return true;
			}
		});
		
		LOG.info("升级执行状态：{}", ret);
		return ret;
	}
	
	private void confirmUpgrade() {
		template.execute("update sign_upgrade set upgraded = '1'");
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
				updateImageId( table.replaceAll("t_", "sn_"), field, id, newId);
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
		
		//==================本机测试用,实际环境要删除!!!========================
		//fileInfo = "F:\\testfolderforsignupload" + fileInfo;
		
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
	
	//更新dep_order
	private void updateDepOrders(String table) throws IOException {
		List<String> ids = getOrgIds(table,"sign_id");
		LOG.debug("get org_id from  {} ", table);
		String org_id = getOrgId("t_sign");
		List<MyDepartment> mdList = null;
		try {
			Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("orgUuid", org_id);
			mdList = accessService.getDepartments(org_id);
			LOG.debug("===获取departmentList成功=== ");
		} catch (Exception e) {
			LOG.error("获取org_id异常=>orgid不存在或不唯一 ：{}", e);
		}
		for (String sign_id : ids) {
			if (StringUtils.isNotEmpty(sign_id)) {
				LOG.debug("search dep_order for sign_id: {} ", sign_id);
				String depid = getDepIdsBySignId(table,sign_id);
				String deporder = "";
				if(depid != null){
					deporder = getDepOrder(org_id,depid,mdList);
				}
				updateDepOrder( table.replaceAll("t_", "sn_"), sign_id, deporder);
			}
		}
	}
	
	private List<String> getOrgIds(String table, String field) {
		return template.query("select " + field + " from " + table, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});
	}
	
	private String getDepIdsBySignId(String table,String sign_id) {
		return template.query("select dep_id from " + table + " where sign_id='" + sign_id + "'", new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getString(1);
				}
				return null;
			}
		});
	}
	
	@Autowired
    MplusAccessService     accessService;
	private String getDepOrder(String orgid,String depid,List<MyDepartment> mdList) {
		if (StringUtils.isNotEmpty(orgid) && StringUtils.isNotEmpty(depid) && (mdList != null)) {
			List<String> list = new ArrayList<String>();
	        try
	        {
	        	LOG.debug("获取deptorder入口,orgid = {} ,depid = {} ", orgid,depid);
	            for (MyDepartment mdInfo : mdList)
	            {
	                if (depid.contains(mdInfo.getDepUuid()))
	                {
	                    list.add(mdInfo.getDepOrder());
	                }
	            }
	        }
	        catch (Exception e)
	        {
	        	LOG.error("获取deptorder失败：{}", e);
	        }
	        if(list.size() == 0){
	        	LOG.debug("查询不到deptorder,返回空");
	        	return "";
	        }else{
	        	LOG.debug("获取deptorder成功");
	        	return list.get(0);
	        }
		}else{
			return "";
		}
	}
	
	private void updateDepOrder(String table, String sign_id, String deporder) {
		String sql = "update " + table + " set dep_order ='" + deporder + "' where sign_id ='" + sign_id + "'";
		LOG.debug("update dep_order for sign_id: {}==={}",sign_id , sql);
		template.execute(sql);
	}
}
