package com.fiberhome.mapps.servicemanager.dao;

import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.SmDbAssign;

public interface SmDbAssignMapper extends MyMapper<SmDbAssign> {
	public SmDbAssign getDbAssignById(Map<String, Object> map);
}