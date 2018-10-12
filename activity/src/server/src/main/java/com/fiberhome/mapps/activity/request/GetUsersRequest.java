package com.fiberhome.mapps.activity.request;

import com.rop.AbstractRopRequest;

public class GetUsersRequest extends AbstractRopRequest
{
    private String userName;

    private String loginId;

    private String depUuid;
    /** 部门范围，0表示当前部门成员，1表示部门及子部门所有成员，默认为0。 */
    private String depScope;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getLoginId()
    {
        return loginId;
    }

    public void setLoginId(String loginId)
    {
        this.loginId = loginId;
    }

    public String getDepUuid()
    {
        return depUuid;
    }

    public void setDepUuid(String depUuid)
    {
        this.depUuid = depUuid;
    }

    public String getDepScope()
    {
        return depScope;
    }

    public void setDepScope(String depScope)
    {
        this.depScope = depScope;
    }
}
