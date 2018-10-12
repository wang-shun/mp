package com.fiberhome.mapps.servicemanager.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.SmResourceInfoitem;

public interface SmResourceInfoitemMapper extends MyMapper<SmResourceInfoitem> {
	public List<SmResourceInfoitem> getTypeItem(Map<String, Object> map);
}