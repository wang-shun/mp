package com.fiberhome.mapps.servicemanager.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.RsKeyValue;
import com.fiberhome.mapps.servicemanager.entity.SmAppConfig;

public interface SmAppConfigMapper extends MyMapper<SmAppConfig> {
	public Long getConfigVersion(Map<String, Object> map);
	
	public void inactiveConfig(Map<String, Object> map);
	
	public List<RsKeyValue> getConfigInfo(Map<String, Object> map);
}