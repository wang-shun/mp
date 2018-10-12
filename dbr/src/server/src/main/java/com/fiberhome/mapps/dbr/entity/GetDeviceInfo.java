package com.fiberhome.mapps.dbr.entity;

import java.util.Date;

public class GetDeviceInfo
{
    private String id;

    private String ecid;

    private String userId;

    private String userName;

    private String deviceId;

    private String deviceName;

    private Date   createTime;

    private String createTimeStr;

    private String deviceStatus;

    private String borrowUserId;

    private String borrowUserName;

    private Date   borrowTime;

    private String borrowTimeStr;

    private String logId;

    private String adminFlag;

    public String getAdminFlag()
    {
        return adminFlag;
    }

    public void setAdminFlag(String adminFlag)
    {
        this.adminFlag = adminFlag;
    }

    public String getLogId()
    {
        return logId;
    }

    public void setLogId(String logId)
    {
        this.logId = logId;
    }

    public String getCreateTimeStr()
    {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr)
    {
        this.createTimeStr = createTimeStr;
    }

    public String getBorrowUserId()
    {
        return borrowUserId;
    }

    public void setBorrowUserId(String borrowUserId)
    {
        this.borrowUserId = borrowUserId;
    }

    public String getBorrowUserName()
    {
        return borrowUserName;
    }

    public void setBorrowUserName(String borrowUserName)
    {
        this.borrowUserName = borrowUserName;
    }

    public Date getBorrowTime()
    {
        return borrowTime;
    }

    public void setBorrowTime(Date borrowTime)
    {
        this.borrowTime = borrowTime;
    }

    public String getBorrowTimeStr()
    {
        return borrowTimeStr;
    }

    public void setBorrowTimeStr(String borrowTimeStr)
    {
        this.borrowTimeStr = borrowTimeStr;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getEcid()
    {
        return ecid;
    }

    public void setEcid(String ecid)
    {
        this.ecid = ecid;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getDeviceStatus()
    {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus)
    {
        this.deviceStatus = deviceStatus;
    }
}