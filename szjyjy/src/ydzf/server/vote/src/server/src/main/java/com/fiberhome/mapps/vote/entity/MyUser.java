package com.fiberhome.mapps.vote.entity;

public class MyUser
{
    private String userUuid;
    private String userName;
    private String deptUuid;
    private String deptName;
    private String phoneNum;
    private String loginId;
    public String getDeptUuid()
    {
        return deptUuid;
    }

    public void setDeptUuid(String deptUuid)
    {
        this.deptUuid = deptUuid;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getPhoneNum()
    {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum)
    {
        this.phoneNum = phoneNum;
    }

    public String getUserUuid()
    {
        return userUuid;
    }

    public void setUserUuid(String userUuid)
    {
        this.userUuid = userUuid;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
    
}
