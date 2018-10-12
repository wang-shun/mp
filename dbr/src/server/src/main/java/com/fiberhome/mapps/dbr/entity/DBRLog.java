package com.fiberhome.mapps.dbr.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dbr_borrow_return_log")
public class DBRLog
{
    @Id
    private String id;

    @Column(name = "device_id")
    private String deviceId;

    private String ecid;

    @Column(name = "dept_name")
    private String deptName;

    @Column(name = "dept_id")
    private String deptId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "log_flag")
    private String logFlag;

    @Column(name = "borrow_time")
    private Date   borrowTime;

    @Column(name = "return_time")
    private Date   returnTime;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getEcid()
    {
        return ecid;
    }

    public void setEcid(String ecid)
    {
        this.ecid = ecid;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getDeptId()
    {
        return deptId;
    }

    public void setDeptId(String deptId)
    {
        this.deptId = deptId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getLogFlag()
    {
        return logFlag;
    }

    public void setLogFlag(String logFlag)
    {
        this.logFlag = logFlag;
    }

    public Date getBorrowTime()
    {
        return borrowTime;
    }

    public void setBorrowTime(Date borrowTime)
    {
        this.borrowTime = borrowTime;
    }

    public Date getReturnTime()
    {
        return returnTime;
    }

    public void setReturnTime(Date returnTime)
    {
        this.returnTime = returnTime;
    }

}