package com.fiberhome.mapps.servicemanager.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.McDashboard;

public interface McDashboardMapper extends MyMapper<McDashboard> {
	public List<McDashboard> getDashboardList(Map<String, Object> map);
}