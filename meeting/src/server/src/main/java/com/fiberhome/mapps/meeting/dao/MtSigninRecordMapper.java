package com.fiberhome.mapps.meeting.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meeting.entity.MtSigninRecord;

public interface MtSigninRecordMapper extends MyMapper<MtSigninRecord>
{
    public void deleteByMeetingId(Map<String, Object> map);

    public List<MtSigninRecord> getByMeetingId(Map<String, Object> map);

    public void updateSignRecord(Map<String, Object> map);

    public List<MtSigninRecord> getSignList(Map<String, Object> map);

    public MtSigninRecord getUpdateData(Map<String, Object> map);

    public List<MtSigninRecord> getBySequId(Map<String, Object> map);

    public List<MtSigninRecord> getSignPerson(Map<String, Object> map);

    public List<LinkedHashMap<String, Object>> getSignDetail(Map<String, Object> map);
}