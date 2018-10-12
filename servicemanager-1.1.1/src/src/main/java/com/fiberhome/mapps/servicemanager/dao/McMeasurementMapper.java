package com.fiberhome.mapps.servicemanager.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.McMeasurement;

public interface McMeasurementMapper extends MyMapper<McMeasurement> {
	public List<McMeasurement> getMeasurementList(Map<String, Object> map);
}