package com.fiberhome.mapps.meetingroom.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meetingroom.entity.OpLog;

public interface OpLogMapper extends MyMapper<OpLog>
{
    public List<OpLog> getOpLog(Map<String, Object> map);
}