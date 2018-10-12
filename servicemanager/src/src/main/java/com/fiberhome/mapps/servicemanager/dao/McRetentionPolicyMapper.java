package com.fiberhome.mapps.servicemanager.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.McRetentionPolicy;

public interface McRetentionPolicyMapper extends MyMapper<McRetentionPolicy> {
	public List<McRetentionPolicy> getQueryList(Map<String, Object> map);
}