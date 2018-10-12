package com.fiberhome.mapps.contact.pojo;

import java.util.Map;

public class MobilearkUser
{
    private String              depUuid;
    private String              userUuid;
    private String              userName;
    private String              loginId;
    private String              phoneNumber;
    private String              emailAddress;
    private String              department;
    private String              memo;
    private Integer             handsetNum;
    private Integer             appNum;
    private Integer             userStatus;
    private Map<String, String> userAttrs;
    private String              avatarUrl;
    private Long                updateTime;
    private Integer             userWeight;

    public Integer getUserWeight()
    {
        return userWeight;
    }

    public void setUserWeight(Integer userWeight)
    {
        this.userWeight = userWeight;
    }

    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl)
    {
        this.avatarUrl = avatarUrl;
    }

    public Long getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getDepUuid()
    {
        return depUuid;
    }

    public void setDepUuid(String depUuid)
    {
        this.depUuid = depUuid;
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

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public Integer getHandsetNum()
    {
        return handsetNum;
    }

    public void setHandsetNum(Integer handsetNum)
    {
        this.handsetNum = handsetNum;
    }

    public Integer getAppNum()
    {
        return appNum;
    }

    public void setAppNum(Integer appNum)
    {
        this.appNum = appNum;
    }

    public Integer getUserStatus()
    {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus)
    {
        this.userStatus = userStatus;
    }

    public Map<String, String> getUserAttrs()
    {
        return userAttrs;
    }

    public void setUserAttrs(Map<String, String> userAttrs)
    {
        this.userAttrs = userAttrs;
    }
}
