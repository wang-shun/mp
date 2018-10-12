package com.fiberhome.mapps.meeting.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meeting.entity.MtAttachment;

public interface MtAttachmentMapper extends MyMapper<MtAttachment>
{
    public void deleteByMeetingId(Map<String, Object> map);

    public List<MtAttachment> getByMeetingId(Map<String, Object> map);
}