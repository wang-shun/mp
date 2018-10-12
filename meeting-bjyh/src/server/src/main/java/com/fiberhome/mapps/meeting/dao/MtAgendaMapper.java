package com.fiberhome.mapps.meeting.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meeting.entity.MtAgenda;

public interface MtAgendaMapper extends MyMapper<MtAgenda>
{
    public void deleteByMeetingId(Map<String, Object> map);

    public List<MtAgenda> getByMeetingId(Map<String, Object> map);

    public List<String> getDayByMeetingId(Map<String, Object> map);
}