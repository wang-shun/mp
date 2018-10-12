package com.fiberhome.mapps.meeting.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ReservedDetail
{
    @XmlElement(name = "reservedId")
    private String  reservedId;
    @XmlElement(name = "reservedUserId")
    private String  reservedUserId;
    @XmlElement(name = "reservedUserName")
    private String  reservedUserName;
    @XmlElement(name = "reservedRemark")
    private String  reservedRemark;
    @XmlElement(name = "meetingName")
    private String  meetingName;
    @XmlElement(name = "starttime")
    private String  starttime;
    @XmlElement(name = "endtime")
    private String  endtime;
    @XmlElement(name = "phoneNum")
    private String  phoneNum;
    @XmlElement(name = "reservedDate")
    private String  reservedDate;
    @XmlElement(name = "roomName")
    private String  roomName;
    @XmlElement(name = "isApprove")
    private boolean isApprove;
    @XmlElement(name = "approveResult")
    private String  approveResult;
    @XmlElement(name = "status")
    private String  status;
    @XmlElement(name = "deptName")
    private String  deptName;

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getReservedId()
    {
        return reservedId;
    }

    public void setReservedId(String reservedId)
    {
        this.reservedId = reservedId;
    }

    public String getApproveResult()
    {
        return approveResult;
    }

    public void setApproveResult(String approveResult)
    {
        this.approveResult = approveResult;
    }

    public boolean isApprove()
    {
        return isApprove;
    }

    public void setApprove(boolean isApprove)
    {
        this.isApprove = isApprove;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public String getReservedDate()
    {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate)
    {
        this.reservedDate = reservedDate;
    }

    public String getPhoneNum()
    {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum)
    {
        this.phoneNum = phoneNum;
    }

    public String getReservedUserId()
    {
        return reservedUserId;
    }

    public void setReservedUserId(String reservedUserId)
    {
        this.reservedUserId = reservedUserId;
    }

    public String getReservedUserName()
    {
        return reservedUserName;
    }

    public void setReservedUserName(String reservedUserName)
    {
        this.reservedUserName = reservedUserName;
    }

    public String getReservedRemark()
    {
        return reservedRemark;
    }

    public void setReservedRemark(String reservedRemark)
    {
        this.reservedRemark = reservedRemark;
    }

    public String getMeetingName()
    {
        return meetingName;
    }

    public void setMeetingName(String meetingName)
    {
        this.meetingName = meetingName;
    }

    public String getStarttime()
    {
        return starttime;
    }

    public void setStarttime(String starttime)
    {
        this.starttime = starttime;
    }

    public String getEndtime()
    {
        return endtime;
    }

    public void setEndtime(String endtime)
    {
        this.endtime = endtime;
    }
}
