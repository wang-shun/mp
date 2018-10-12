package com.fiberhome.mapps.meeting.entity;

import javax.persistence.Column;

public class ClientMeetingInfo
{
    private String meetingId;

    private String meetingName;

    private String address;

    private String holderName;

    private String beginTimeStr;

    private String endTimeStr;

    private String createTimeStr;

    private String isSelfCreate;

    private String isSelfJoin;

    private String isSelfService;

    private String selfOp;

    private String hasGroup;

    private String groupId;
    
    //签到方式:正向/反向
    private String signType;
    
    //会议提醒
    private String  noticeType;
    private Integer noticeSet;

    /**
     * 草稿：10 未进行：20 进行中：30 已取消：40 已结束：50
     */
    private String status;

    private String sponsor;

    private String person;

    private String tel;

    public String getSponsor()
    {
        return sponsor;
    }

    public void setSponsor(String sponsor)
    {
        this.sponsor = sponsor;
    }

    public String getPerson()
    {
        return person;
    }

    public void setPerson(String person)
    {
        this.person = person;
    }

    public String getTel()
    {
        return tel;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public String getIsSelfJoin()
    {
        return isSelfJoin;
    }

    public void setIsSelfJoin(String isSelfJoin)
    {
        this.isSelfJoin = isSelfJoin;
    }

    public String getIsSelfService()
    {
        return isSelfService;
    }

    public void setIsSelfService(String isSelfService)
    {
        this.isSelfService = isSelfService;
    }

    public String getHasGroup()
    {
        return hasGroup;
    }

    public void setHasGroup(String hasGroup)
    {
        this.hasGroup = hasGroup;
    }

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    public String getMeetingId()
    {
        return meetingId;
    }

    public void setMeetingId(String meetingId)
    {
        this.meetingId = meetingId;
    }

    public String getMeetingName()
    {
        return meetingName;
    }

    public void setMeetingName(String meetingName)
    {
        this.meetingName = meetingName;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getBeginTimeStr()
    {
        return beginTimeStr;
    }

    public void setBeginTimeStr(String beginTimeStr)
    {
        this.beginTimeStr = beginTimeStr;
    }

    public String getEndTimeStr()
    {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr)
    {
        this.endTimeStr = endTimeStr;
    }

    public String getIsSelfCreate()
    {
        return isSelfCreate;
    }

    public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public void setIsSelfCreate(String isSelfCreate)
    {
        this.isSelfCreate = isSelfCreate;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getCreateTimeStr()
    {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr)
    {
        this.createTimeStr = createTimeStr;
    }

    public String getSelfOp()
    {
        return selfOp;
    }

    public void setSelfOp(String selfOp)
    {
        this.selfOp = selfOp;
    }

    public String getHolderName()
    {
        return holderName;
    }

    public void setHolderName(String holderName)
    {
        this.holderName = holderName;
    }

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public Integer getNoticeSet() {
		return noticeSet;
	}

	public void setNoticeSet(Integer noticeSet) {
		this.noticeSet = noticeSet;
	}

}
