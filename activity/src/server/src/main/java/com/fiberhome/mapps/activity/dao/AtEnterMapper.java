package com.fiberhome.mapps.activity.dao;

import java.util.Map;

import com.fiberhome.mapps.activity.entity.AtEnter;
import com.fiberhome.mapps.intergration.mybatis.MyMapper;

public interface AtEnterMapper extends MyMapper<AtEnter> {
	public int getEnterCount(Map<String, Object> map);
}