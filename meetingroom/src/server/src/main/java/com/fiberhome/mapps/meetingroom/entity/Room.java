package com.fiberhome.mapps.meetingroom.entity;

import java.util.List;

public class Room
{
    private String               roomId;
    private String               roomName;
    private List<ReservedDetail> reservedDetailList;

    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public List<ReservedDetail> getReservedDetailList()
    {
        return reservedDetailList;
    }

    public void setReservedDetailList(List<ReservedDetail> reservedDetailList)
    {
        this.reservedDetailList = reservedDetailList;
    }
}
