package com.fiberhome.mapps.sign.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class GetUserIconRequest extends AbstractRopRequest
{
    @NotNull
    private String userIds;

    public String getUserIds()
    {
        return userIds;
    }

    public void setUserIds(String userIds)
    {
        this.userIds = userIds;
    }
}
