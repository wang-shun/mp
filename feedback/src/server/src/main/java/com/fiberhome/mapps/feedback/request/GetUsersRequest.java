package com.fiberhome.mapps.feedback.request;

import com.rop.AbstractRopRequest;

public class GetUsersRequest extends AbstractRopRequest
{
    private String userName;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }
}
