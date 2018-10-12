package com.fiberhome.mapps.servicemanager.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.ResourceAssignInfo;
import com.fiberhome.mapps.servicemanager.entity.SmResourceAssign;

public interface SmResourceAssignMapper extends MyMapper<SmResourceAssign> {
	public List<ResourceAssignInfo> getResourceListByAppId(Map<String, Object> map);
}