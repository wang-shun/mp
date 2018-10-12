package com.fiberhome.mapps.meeting.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ReservedInfo
{
    @XmlElement(name = "loginId")
    private String loginId;
    @XmlElement(name = "userName")
    private String userName;
    @XmlElement(name = "tel")
    private String tel;
    @XmlElement(name = "meetingName")
    private String meetingName;
    @XmlElement(name = "beginTime")
    private Date   beginTime;
    @XmlElement(name = "endTime")
    private Date   endTime;
    @XmlElement(name = "beginTimeStr")
    private String beginTimeStr;
    @XmlElement(name = "endTimeStr")
    private String endTimeStr;
    @XmlElement(name = "meetingId")
    private String meetingId;
    @XmlElement(name = "statusFlag")
    private String statusFlag;

    public String getMeetingId()
    {
        return meetingId;
    }

    public void setMeetingId(String meetingId)
    {
        this.meetingId = meetingId;
    }

    public String getLoginId()
    {
        return loginId;
    }

    public void setLoginId(String loginId)
    {
        this.loginId = loginId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getTel()
    {
        return tel;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public String getMeetingName()
    {
        return meetingName;
    }

    public void setMeetingName(String meetingName)
    {
        this.meetingName = meetingName;
    }

    public Date getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(Date beginTime)
    {
        this.beginTime = beginTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
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
}
