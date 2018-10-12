package com.fiberhome.mapps.meetingroom.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class QueryRoomDetailRequest extends AbstractRopRequest
{
    @NotNull
    private String roomId;

    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

}
