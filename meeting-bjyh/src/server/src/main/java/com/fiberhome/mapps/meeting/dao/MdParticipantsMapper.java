package com.fiberhome.mapps.meeting.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meeting.entity.MdParticipants;

public interface MdParticipantsMapper extends MyMapper<MdParticipants>
{
    public void deleteByMeetingId(Map<String, Object> map);

    public List<MdParticipants> getByMeetingId(Map<String, Object> map);
}