package com.fiberhome.mapps.servicemanager.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.ClientDatabaseInfo;
import com.fiberhome.mapps.servicemanager.entity.SmDatabase;

public interface SmDatabaseMapper extends MyMapper<SmDatabase> {
	public SmDatabase getDatabaseDetailById(Map<String, Object> map);
	
	public void stopDatabase(Map<String, Object> map);
	
	public void startDatabase(Map<String, Object> map);
	
	public void testDatabase(Map<String, Object> map);
	
	public List<ClientDatabaseInfo> getDatabaseList(Map<String, Object> map);
	
	
}