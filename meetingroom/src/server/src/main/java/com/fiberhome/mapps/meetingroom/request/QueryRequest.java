package com.fiberhome.mapps.meetingroom.request;

import com.rop.AbstractRopRequest;

public class QueryRequest extends AbstractRopRequest
{
    private String roomId;
    private String reservedDate;
    private String reservedId;

    public String getReservedId()
    {
        return reservedId;
    }

    public void setReservedId(String reservedId)
    {
        this.reservedId = reservedId;
    }

    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

    public String getReservedDate()
    {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate)
    {
        this.reservedDate = reservedDate;
    }

}
