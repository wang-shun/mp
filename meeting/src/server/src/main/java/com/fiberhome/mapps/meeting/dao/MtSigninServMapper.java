package com.fiberhome.mapps.meeting.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meeting.entity.MtSigninServ;

public interface MtSigninServMapper extends MyMapper<MtSigninServ>
{
    public void deleteByMeetingId(Map<String, Object> map);

    public List<MtSigninServ> getByMeetingId(Map<String, Object> map);
}