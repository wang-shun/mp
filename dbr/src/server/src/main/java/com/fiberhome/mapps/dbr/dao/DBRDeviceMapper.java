package com.fiberhome.mapps.dbr.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.dbr.entity.DBRDevice;
import com.fiberhome.mapps.dbr.entity.GetDeviceInfo;
import com.fiberhome.mapps.intergration.mybatis.MyMapper;

public interface DBRDeviceMapper extends MyMapper<DBRDevice>
{
    public List<GetDeviceInfo> getDevice(Map<String, Object> map);
}