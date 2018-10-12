package com.fiberhome.mapps.meeting.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class GetLockReservedTimeRequest extends AbstractRopRequest
{
    @NotNull
    private String roomId;
    @NotNull
    private String timeStart;
    @NotNull
    private String timeEnd;

    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

    public String getTimeStart()
    {
        return timeStart;
    }

    public void setTimeStart(String timeStart)
    {
        this.timeStart = timeStart;
    }

    public String getTimeEnd()
    {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd)
    {
        this.timeEnd = timeEnd;
    }

}
