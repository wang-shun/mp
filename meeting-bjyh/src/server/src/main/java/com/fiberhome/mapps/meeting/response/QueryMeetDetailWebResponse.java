package com.fiberhome.mapps.meeting.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.meeting.entity.ClientMeetingInfo;
import com.fiberhome.mapps.meeting.entity.MdParticipants;
import com.fiberhome.mapps.meeting.entity.MtAgenda;
import com.fiberhome.mapps.meeting.entity.MtAttachment;
import com.fiberhome.mapps.meeting.entity.MtRemarks;
import com.fiberhome.mapps.meeting.entity.MtSigninRecord;
import com.fiberhome.mapps.meeting.entity.MtSigninSequ;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryMeetDetailWebResponse extends BaseResponse
{
    @XmlElement(name = "meeting")
    private ClientMeetingInfo      meeting;
    // 会议议程 MtAgenda
    @XmlElement(name = "agendasInfo")
    private List<MtAgenda> agendaList;
    // 附件资料 MtAttachment
    @XmlElement(name = "attachmentList")
    private List<MtAttachment>     attachmentList;
    // 会议签到 MtSigninSequ
    @XmlElement(name = "signinSequList")
    private List<MtSigninSequ>     signinSequList;
    @XmlElement(name = "signinRecordList")
    private List<MtSigninRecord>   signinRecordList;
    // 会议备注 MtRemarks
    @XmlElement(name = "remarksList")
    private List<MtRemarks>        remarksList;
    @XmlElement(name = "qrcode")
    private String qrcode;
    // 参会人员和服务人员信息
    @XmlElement(name = "mdList")
    private List<MdParticipants>        mdList;
    public ClientMeetingInfo getMeeting()
    {
        return meeting;
    }

    public void setMeeting(ClientMeetingInfo meeting)
    {
        this.meeting = meeting;
    }


    public List<MtAgenda> getAgendaList() {
		return agendaList;
	}

	public void setAgendaList(List<MtAgenda> agendaList) {
		this.agendaList = agendaList;
	}

	public List<MtAttachment> getAttachmentList()
    {
        return attachmentList;
    }

    public void setAttachmentList(List<MtAttachment> attachmentList)
    {
        this.attachmentList = attachmentList;
    }

    public List<MtSigninSequ> getSigninSequList()
    {
        return signinSequList;
    }

    public void setSigninSequList(List<MtSigninSequ> signinSequList)
    {
        this.signinSequList = signinSequList;
    }

    public List<MtSigninRecord> getSigninRecordList()
    {
        return signinRecordList;
    }

    public void setSigninRecordList(List<MtSigninRecord> signinRecordList)
    {
        this.signinRecordList = signinRecordList;
    }

    public List<MtRemarks> getRemarksList()
    {
        return remarksList;
    }

    public void setRemarksList(List<MtRemarks> remarksList)
    {
        this.remarksList = remarksList;
    }

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public List<MdParticipants> getMdList() {
		return mdList;
	}

	public void setMdList(List<MdParticipants> mdList) {
		this.mdList = mdList;
	}
    
}
