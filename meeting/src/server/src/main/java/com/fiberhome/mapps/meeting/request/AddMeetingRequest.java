package com.fiberhome.mapps.meeting.request;

import java.util.List;

import com.fiberhome.mapps.meeting.entity.MdParticipants;
import com.fiberhome.mapps.meeting.entity.MtAgenda;
import com.fiberhome.mapps.meeting.entity.MtAttachment;
import com.fiberhome.mapps.meeting.entity.MtMeeting;
import com.fiberhome.mapps.meeting.entity.MtRemarks;
import com.fiberhome.mapps.meeting.entity.ClientMtSigninSequInfo;
import com.rop.AbstractRopRequest;

public class AddMeetingRequest extends AbstractRopRequest
{
    private String               meetingJson;
    // 会议信息 MtMeeting
    private MtMeeting            meeting;
    // 参会人员 内部、外部、服务 MdParticipants
    private List<MdParticipants> inParticipantsList;
    private List<MdParticipants> outParticipantsList;
    private List<MdParticipants> serviceParticipantsList;

    // 会议议程 MtAgenda
    private String               agendaFlag;
    private List<MtAgenda>       agendaList;
    // 附件资料 MtAttachment
    private String               attachmentFlag;
    private List<MtAttachment>   attachmentList;
    // 创建群组 调用im创建群组接口
    private String               createGroupFlag;
    // 会议签到 MtSigninSequ
    private String               signinFlag;
    private List<ClientMtSigninSequInfo>   signinSequList;
    // 会议备注 MtRemarks
    private String               remarksFlag;
    private List<MtRemarks>      remarksList;

    public String getMeetingJson()
    {
        return meetingJson;
    }

    public void setMeetingJson(String meetingJson)
    {
        this.meetingJson = meetingJson;
    }

    public MtMeeting getMeeting()
    {
        return meeting;
    }

    public void setMeeting(MtMeeting meeting)
    {
        this.meeting = meeting;
    }

    public List<MdParticipants> getInParticipantsList()
    {
        return inParticipantsList;
    }

    public void setInParticipantsList(List<MdParticipants> inParticipantsList)
    {
        this.inParticipantsList = inParticipantsList;
    }

    public List<MdParticipants> getOutParticipantsList()
    {
        return outParticipantsList;
    }

    public void setOutParticipantsList(List<MdParticipants> outParticipantsList)
    {
        this.outParticipantsList = outParticipantsList;
    }

    public List<MdParticipants> getServiceParticipantsList()
    {
        return serviceParticipantsList;
    }

    public void setServiceParticipantsList(List<MdParticipants> serviceParticipantsList)
    {
        this.serviceParticipantsList = serviceParticipantsList;
    }

    public String getAgendaFlag()
    {
        return agendaFlag;
    }

    public void setAgendaFlag(String agendaFlag)
    {
        this.agendaFlag = agendaFlag;
    }

    public List<MtAgenda> getAgendaList()
    {
        return agendaList;
    }

    public void setAgendaList(List<MtAgenda> agendaList)
    {
        this.agendaList = agendaList;
    }

    public String getAttachmentFlag()
    {
        return attachmentFlag;
    }

    public void setAttachmentFlag(String attachmentFlag)
    {
        this.attachmentFlag = attachmentFlag;
    }

    public List<MtAttachment> getAttachmentList()
    {
        return attachmentList;
    }

    public void setAttachmentList(List<MtAttachment> attachmentList)
    {
        this.attachmentList = attachmentList;
    }

    public String getCreateGroupFlag()
    {
        return createGroupFlag;
    }

    public void setCreateGroupFlag(String createGroupFlag)
    {
        this.createGroupFlag = createGroupFlag;
    }

    public String getSigninFlag()
    {
        return signinFlag;
    }

    public void setSigninFlag(String signinFlag)
    {
        this.signinFlag = signinFlag;
    }

    public List<ClientMtSigninSequInfo> getSigninSequList()
    {
        return signinSequList;
    }

    public void setSigninSequList(List<ClientMtSigninSequInfo> signinSequList)
    {
        this.signinSequList = signinSequList;
    }

    public String getRemarksFlag()
    {
        return remarksFlag;
    }

    public void setRemarksFlag(String remarksFlag)
    {
        this.remarksFlag = remarksFlag;
    }

    public List<MtRemarks> getRemarksList()
    {
        return remarksList;
    }

    public void setRemarksList(List<MtRemarks> remarksList)
    {
        this.remarksList = remarksList;
    }

}
