package com.fiberhome.mapps.servicemanager.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.SmResource;

public interface SmResourceMapper extends MyMapper<SmResource> {
	public List<SmResource> getResourceList(Map<String, Object> map);
	
	public SmResource getResourceDetailById(Map<String, Object> map);
	
	public void disableResourceById(Map<String, Object> map);
	
	public void enableResourceById(Map<String, Object> map);
}