package com.fiberhome.mapps.meetingroom.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class AddPrivilegeRequest extends AbstractRopRequest
{
    @NotNull
    private String roomId;
    private String jsonData;

    public String getJsonData()
    {
        return jsonData;
    }

    public void setJsonData(String jsonData)
    {
        this.jsonData = jsonData;
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
