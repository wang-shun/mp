package com.fiberhome.mapps.servicemanager.dao;

import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.SmResourceConfig;

public interface SmResourceConfigMapper extends MyMapper<SmResourceConfig> {
	public Long getConfigVersion(Map<String, Object> map);
}