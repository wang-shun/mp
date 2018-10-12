package com.fiberhome.mapps.servicemanager.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.ClientRedisInfo;
import com.fiberhome.mapps.servicemanager.entity.SmRedis;

public interface SmRedisMapper extends MyMapper<SmRedis> {
	public List<ClientRedisInfo> getRedisList(Map<String, Object> map);
	
	public SmRedis getRedisDetailById(Map<String, Object> map);
	
	public void disableRedisById(Map<String, Object> map);
	
	public void enableRedisById(Map<String, Object> map);
}