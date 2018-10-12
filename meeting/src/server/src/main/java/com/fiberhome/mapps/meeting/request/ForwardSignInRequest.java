package com.fiberhome.mapps.meeting.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class ForwardSignInRequest extends AbstractRopRequest
{

    @NotNull
    private String meetingId;

    @NotNull
    private String sequId;

    public String getSequId()
    {
        return sequId;
    }

    public void setSequId(String sequId)
    {
        this.sequId = sequId;
    }

    public String getMeetingId()
    {
        return meetingId;
    }

    public void setMeetingId(String meetingId)
    {
        this.meetingId = meetingId;
    }

}
