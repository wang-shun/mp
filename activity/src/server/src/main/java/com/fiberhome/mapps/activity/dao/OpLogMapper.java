package com.fiberhome.mapps.activity.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.activity.entity.OpLog;
import com.fiberhome.mapps.intergration.mybatis.MyMapper;


public interface OpLogMapper extends MyMapper<OpLog>
{
    public List<OpLog> getOpLog(Map<String, Object> map);
}