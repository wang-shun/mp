package com.fiberhome.mapps.meeting.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meeting.entity.MtParticipants;

public interface MtParticipantsMapper extends MyMapper<MtParticipants>
{
    public void deleteByMeetingId(Map<String, Object> map);

    public List<MtParticipants> getByMeetingId(Map<String, Object> map);

    public MtParticipants getParticipant(Map<String, Object> map);

    public MtParticipants getParticipantService(Map<String, Object> map);

    public MtParticipants getParById(Map<String, Object> map);

    public List<MtParticipants> getNoSendInnerByMeetingId(Map<String, Object> map);

    public List<MtParticipants> getNoSendOuterByMeetingId(Map<String, Object> map);

    public void updateStatusToNoSend(Map<String, Object> map);

    public void updateStatusToSend(Map<String, Object> map);

    public void updateStatusToSent(Map<String, Object> map);

    public void setMeetVisable(Map<String, Object> map);

    public List<MtParticipants> getNoQrcode();

    public void insertBatch(List<MtParticipants> list);
}