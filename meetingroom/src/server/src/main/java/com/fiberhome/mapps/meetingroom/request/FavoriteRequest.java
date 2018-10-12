package com.fiberhome.mapps.meetingroom.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.rop.AbstractRopRequest;

public class FavoriteRequest extends AbstractRopRequest
{
    @NotNull
    private String roomId;
    @NotNull
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String operationType;

    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

    public String getOperationType()
    {
        return operationType;
    }

    public void setOperationType(String operationType)
    {
        this.operationType = operationType;
    }

}
