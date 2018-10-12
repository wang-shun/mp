package com.fiberhome.mapps.servicemanager.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.ClientAlertlogInfo;
import com.fiberhome.mapps.servicemanager.entity.McAlertLogs;

public interface McAlertLogsMapper extends MyMapper<McAlertLogs> {
	public List<ClientAlertlogInfo> getAlertlogsList(Map<String, Object> map);
}