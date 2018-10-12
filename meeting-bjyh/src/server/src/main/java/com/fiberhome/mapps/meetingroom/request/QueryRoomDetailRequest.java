package com.fiberhome.mapps.meetingroom.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class QueryRoomDetailRequest extends AbstractRopRequest
{
    @NotNull
    private String roomId;

    private String selectMonth;

    public String getSelectMonth()
    {
        return selectMonth;
    }

    public void setSelectMonth(String selectMonth)
    {
        this.selectMonth = selectMonth;
    }

    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

}
