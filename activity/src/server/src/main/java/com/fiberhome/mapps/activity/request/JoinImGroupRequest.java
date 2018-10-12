package com.fiberhome.mapps.activity.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class JoinImGroupRequest extends AbstractRopRequest
{
    @NotNull
    private String actId;
    @NotNull
    private String groupId;
    private String members;
    private String username;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getActId()
    {
        return actId;
    }

    public void setActId(String actId)
    {
        this.actId = actId;
    }

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
