package com.fiberhome.mapps.meeting.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meeting.entity.MtRemarks;

public interface MtRemarksMapper extends MyMapper<MtRemarks>
{
    public void deleteByMeetingId(Map<String, Object> map);

    public List<MtRemarks> getByMeetingId(Map<String, Object> map);
}