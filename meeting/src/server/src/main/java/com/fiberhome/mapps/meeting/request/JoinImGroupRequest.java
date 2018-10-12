package com.fiberhome.mapps.meeting.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class JoinImGroupRequest extends AbstractRopRequest
{
    @NotNull
    private String groupId;
    @NotNull
    private String members;

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    public String getMembers()
    {
        return members;
    }

    public void setMembers(String members)
    {
        this.members = members;
    }
}
