package com.fiberhome.mapps.servicemanager.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.McDownsample;

public interface McDownsampleMapper extends MyMapper<McDownsample> {
	public List<McDownsample> getQueryList(Map<String, Object> map);
}