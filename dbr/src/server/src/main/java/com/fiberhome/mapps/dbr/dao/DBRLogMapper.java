package com.fiberhome.mapps.dbr.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.dbr.entity.DBRLog;
import com.fiberhome.mapps.intergration.mybatis.MyMapper;

public interface DBRLogMapper extends MyMapper<DBRLog>
{
    public List<DBRLog> getOpLog(Map<String, Object> map);
}