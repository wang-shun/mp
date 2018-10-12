package com.fiberhome.mapps.meetingroom.dao;

import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meetingroom.entity.MrApproveInfo;

public interface MrApproveInfoMapper extends MyMapper<MrApproveInfo>
{
    public int getCountByUser(Map<String, Object> map);
}