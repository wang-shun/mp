package com.fiberhome.mapps.meetingroom.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meetingroom.entity.MrPrivilege;

public interface MrPrivilegeMapper extends MyMapper<MrPrivilege>
{
    public List<String> getAllEcid(Map<String, Object> map);
}