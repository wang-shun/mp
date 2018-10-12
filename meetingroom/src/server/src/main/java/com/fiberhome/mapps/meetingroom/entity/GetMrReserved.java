package com.fiberhome.mapps.meetingroom.entity;

public class GetMrReserved
{
    private String  roomId;

    private String  ecid;

    private String  roomName;

    private String  address;

    private String  reservedId;

    private String  meetingName;

    private String  reservedDate;

    private String  reservedStartTime;

    private String  reservedEndTime;

    private String  status;

    private String  displayName;

    private String  depName;

    private String  createTime;
    private String  reservedRemark;
    private String  approved;
    private String  phone;
    private String  reservedUserId;
    private String  approveResult;
    private Integer participantsNum;

    public Integer getParticipantsNum()
    {
        return participantsNum;
    }

    public void setParticipantsNum(Integer participantsNum)
    {
        this.participantsNum = participantsNum;
    }

    public String getApproveResult()
    {
        return approveResult;
    }

    public void setApproveResult(String approveResult)
    {
        this.approveResult = approveResult;
    }

    public String getReservedUserId()
    {
        return reservedUserId;
    }

    public void setReservedUserId(String reservedUserId)
    {
        this.reservedUserId = reservedUserId;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getApproved()
    {
        return approved;
    }

    public void setApproved(String approved)
    {
        this.approved = approved;
    }

    public String getReservedRemark()
    {
        return reservedRemark;
    }

    public void setReservedRemark(String reservedRemark)
    {
        this.reservedRemark = reservedRemark;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

    public String getEcid()
    {
        return ecid;
    }

    public void setEcid(String ecid)
    {
        this.ecid = ecid;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getReservedId()
    {
        return reservedId;
    }

    public void setReservedId(String reservedId)
    {
        this.reservedId = reservedId;
    }

    public String getMeetingName()
    {
        return meetingName;
    }

    public void setMeetingName(String meetingName)
    {
        this.meetingName = meetingName;
    }

    public String getReservedDate()
    {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate)
    {
        this.reservedDate = reservedDate;
    }

    public String getReservedStartTime()
    {
        return reservedStartTime;
    }

    public void setReservedStartTime(String reservedStartTime)
    {
        this.reservedStartTime = reservedStartTime;
    }

    public String getReservedEndTime()
    {
        return reservedEndTime;
    }

    public void setReservedEndTime(String reservedEndTime)
    {
        this.reservedEndTime = reservedEndTime;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDepName()
    {
        return depName;
    }

    public void setDepName(String depName)
    {
        this.depName = depName;
    }
}