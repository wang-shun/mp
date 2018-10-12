package com.fiberhome.mapps.meeting.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meeting.entity.ClientMeetingInfo;
import com.fiberhome.mapps.meeting.entity.MtMeeting;
import com.fiberhome.mapps.meeting.entity.MtSignStatus;

public interface MtMeetingMapper extends MyMapper<MtMeeting>
{
    public List<ClientMeetingInfo> getMeeting(Map<String, Object> map);

    public List<ClientMeetingInfo> getMeetingForWeb(Map<String, Object> map);

    public ClientMeetingInfo getMeetingInfo(Map<String, Object> map);

    public void cancelMeetById(Map<String, Object> map);

    public void overMeetById(Map<String, Object> map);

    public void deleteByMeetingId(Map<String, Object> map);

    public void applyMeetById(Map<String, Object> map);

    // public void updateStatusSchedule(Map<String, Object> map);

    public List<MtMeeting> getMeetingCall(Map<String, Object> map);

    ////// bjyh start //////
    public List<ClientMeetingInfo> getBJYHMeeting(Map<String, Object> map);
    ////// bjyh end //////
    
    //轮询获取签到情况
    public MtSignStatus getMeetingSignStatus(Map<String, Object> map);
}