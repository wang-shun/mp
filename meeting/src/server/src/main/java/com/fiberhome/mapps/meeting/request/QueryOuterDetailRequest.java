package com.fiberhome.mapps.meeting.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class QueryOuterDetailRequest extends AbstractRopRequest
{
    @NotNull
    private String participantsId;

    public String getParticipantsId()
    {
        return participantsId;
    }

    public void setParticipantsId(String participantsId)
    {
        this.participantsId = participantsId;
    }

}
