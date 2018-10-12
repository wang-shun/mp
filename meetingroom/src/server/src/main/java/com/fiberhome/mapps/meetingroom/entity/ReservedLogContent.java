package com.fiberhome.mapps.meetingroom.entity;

public class ReservedLogContent extends BaseOpLogContent
{
    private String roomName;
    private String reservedTime;
    private String meetingName;

    public ReservedLogContent()
    {
    }

    public ReservedLogContent(String roomName, String reservedTime, String meetingName, String message)
    {
        this.setMessage(message);
        this.roomName = roomName;
        this.reservedTime = reservedTime;
        this.meetingName = meetingName;
    }

    public String toString()
    {
        return "roomName=" + roomName + ",reservedTime=" + reservedTime + ",meetingName=" + meetingName + ",message="
                + this.getMessage();
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
}