package com.fiberhome.mapps.servicemanager.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.SmRoute;

public interface SmRouteMapper extends MyMapper<SmRoute> {
	public List<SmRoute> getRouteList(Map<String, Object> map);
	
	public List<SmRoute> getRouteListByServiceId(Map<String, Object> map);
	
	public List<SmRoute> getRouteListByPath(Map<String, Object> map);
	
	public void disableRouteById(Map<String, Object> map);
	
	public void enableRouteById(Map<String, Object> map);
	
	public SmRoute getRouteDetailById(Map<String, Object> map);
	
	public List<SmRoute> getAllRoute(Map<String, Object> map);
}