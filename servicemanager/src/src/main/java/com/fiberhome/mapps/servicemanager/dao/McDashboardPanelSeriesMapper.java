package com.fiberhome.mapps.servicemanager.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.McDashboardPanelSeries;

public interface McDashboardPanelSeriesMapper extends MyMapper<McDashboardPanelSeries> {
	public void insertSelectiveMysql(Map<String, Object> map);
	
	public List<McDashboardPanelSeries> selectMysql(Map<String, Object> map);
}