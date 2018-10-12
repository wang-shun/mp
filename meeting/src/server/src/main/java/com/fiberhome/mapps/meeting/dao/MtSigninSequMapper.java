package com.fiberhome.mapps.meeting.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meeting.entity.ClientMtSigninSequInfo;
import com.fiberhome.mapps.meeting.entity.MtSigninSequ;

public interface MtSigninSequMapper extends MyMapper<MtSigninSequ>
{
    public void deleteByMeetingId(Map<String, Object> map);

    public List<ClientMtSigninSequInfo> getByMeetingId(Map<String, Object> map);

}