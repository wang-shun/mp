package com.fiberhome.mapps.contact.pojo;

import java.util.Map;

public class MyUser
{
    private String userUuid;
    private String userName;
    private String deptUuid;
    private String deptName;
    private String deptFullName;
    private String phoneNum;
    private String loginId;
    private String avatarUrl;
    private String emailAddress;
    private Map<String, String> userAttrs;

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl)
    {
        this.avatarUrl = avatarUrl;
    }

    public String getDeptFullName()
    {
        return deptFullName;
    }

    public void setDeptFullName(String deptFullName)
    {
        this.deptFullName = deptFullName;
    }

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

    public String getLoginId()
    {
        return loginId;
    }

    public void setLoginId(String loginId)
    {
        this.loginId = loginId;
    }

	public Map<String, String> getUserAttrs() {
		return userAttrs;
	}

	public void setUserAttrs(Map<String, String> userAttrs) {
		this.userAttrs = userAttrs;
	}

        

}
