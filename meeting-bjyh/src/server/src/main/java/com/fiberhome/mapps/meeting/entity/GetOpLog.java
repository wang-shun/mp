package com.fiberhome.mapps.meeting.entity;

public class GetOpLog
{
    private String userId;
    private String userName;
    private String depName;
    private String op;
    private String opTime;
    private String roomName;
    private String reservedTime;
    private String meetingName;
    private String result;

    public String getDepName()
    {
        return depName;
    }

    public void setDepName(String depName)
    {
        this.depName = depName;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getOp()
    {
        return op;
    }

    public void setOp(String op)
    {
        this.op = op;
    }

    public String getOpTime()
    {
        return opTime;
    }

    public void setOpTime(String opTime)
    {
        this.opTime = opTime;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public String getReservedTime()
    {
        return reservedTime;
    }

    public void setReservedTime(String reservedTime)
    {
        this.reservedTime = reservedTime;
    }

    public String getMeetingName()
    {
        return meetingName;
    }

    public void setMeetingName(String meetingName)
    {
        this.meetingName = meetingName;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }
}