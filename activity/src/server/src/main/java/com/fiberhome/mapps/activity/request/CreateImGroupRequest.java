package com.fiberhome.mapps.activity.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class CreateImGroupRequest extends AbstractRopRequest
{
    @NotNull
    private String groupName;

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }
}
