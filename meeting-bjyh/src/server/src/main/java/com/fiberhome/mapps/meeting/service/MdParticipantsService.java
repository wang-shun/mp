package com.fiberhome.mapps.meeting.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fiberhome.mapps.meeting.dao.MdParticipantsMapper;
import com.fiberhome.mapps.meeting.dao.MtAgendaMapper;
import com.fiberhome.mapps.meeting.dao.MtAttachmentMapper;
import com.fiberhome.mapps.meeting.dao.MtMeetingMapper;
import com.fiberhome.mapps.meeting.dao.MtParticipantsMapper;
import com.fiberhome.mapps.meeting.dao.MtRemarksMapper;
import com.fiberhome.mapps.meeting.dao.MtSigninSequMapper;
import com.fiberhome.mapps.meeting.dao.MtSigninServMapper;
import com.fiberhome.mapps.meeting.entity.MdParticipants;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean(version = "1.0")
public class MdParticipantsService
{
    protected final Logger     LOGGER       = LoggerFactory.getLogger(getClass());
    @Autowired
    MtMeetingMapper            mtMeetingMapper;
    @Autowired
    MdParticipantsMapper       mdParticipantsMapper;
    @Autowired
    MtAgendaMapper             mtAgendaMapper;
    @Autowired
    MtAttachmentMapper         mtAttachmentMapper;
    @Autowired
    MtSigninSequMapper         mtSigninSequMapper;
    @Autowired
    MtSigninServMapper         mtSigninServMapper;
    @Autowired
    MtRemarksMapper            mtRemarksMapper;
    @Autowired
    MtParticipantsMapper       mtParticipantsMapper;
    @Autowired
    ThirdPartAccessService     thirdPartAccessService;
    /** 草稿：10 */
    public static final String STAUTS_10    = "10";
    /** 未进行：20 */
    public static final String STAUTS_20    = "20";
    /** 进行中：30 */
    public static final String STAUTS_30    = "30";
    /** 已取消：40 */
    public static final String STAUTS_40    = "40";
    /** 已结束：50 */
    public static final String STAUTS_50    = "50";
    /** 开启 1 */
    public static final String FLAG_OPEN    = "1";
    /** 关闭 0 */
    public static final String FLAG_CLOSE   = "0";
    /** 0：未通知 */
    public static final String TYPE_INNER   = "inner";
    /** 1：已发送 */
    public static final String TYPE_OUTER   = "outer";
    /** 2：已送达 */
    public static final String TYPE_SERVICE = "service";

    /**
     * 新增会议信息
     * 
     * @param req
     * @return
     */
    public void convertInfoFormMdParticipants(List<MdParticipants> list, String type)
    {
        if (TYPE_INNER.equals(type))
        {

        }
        else if (TYPE_OUTER.equals(type))
        {

        }
        else if (TYPE_SERVICE.equals(type))
        {

        }
    }

}
