package com.fiberhome.mapps.servicemanager.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.McAlertRule;

public interface McAlertRuleMapper extends MyMapper<McAlertRule> {
	public List<McAlertRule> getQueryList(Map<String, Object> map);
}