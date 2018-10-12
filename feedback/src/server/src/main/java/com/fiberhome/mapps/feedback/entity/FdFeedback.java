package com.fiberhome.mapps.feedback.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "FD_FEEDBACK")
public class FdFeedback
{
    @Id
    private String id;

    private String ecid;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "app_ver")
    private String appVer;

    @Column(name = "app_ver_fmt")
    private String appVerFmt;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "os_ver")
    private String osVer;

    private String feedback;

    private String images;

    private String contack;

    @Column(name = "submit_time")
    private Date   submitTime;

    private String submitTimeStr;

    private String confirm;

    @Column(name = "del_flag")
    private String delFlag;

    private String problem;

    private String solution;

    @Column(name = "confirm_user_id")
    private String confirmUserId;

    @Column(name = "confirm_user_name")
    private String confirmUserName;

    @Column(name = "confirm_time")
    private Date   confirmTime;

    public Date getConfirmTime()
    {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime)
    {
        this.confirmTime = confirmTime;
    }

    public String getConfirmUserId()
    {
        return confirmUserId;
    }

    public void setConfirmUserId(String confirmUserId)
    {
        this.confirmUserId = confirmUserId;
    }

    public String getConfirmUserName()
    {
        return confirmUserName;
    }

    public void setConfirmUserName(String confirmUserName)
    {
        this.confirmUserName = confirmUserName;
    }

    public String getProblem()
    {
        return problem;
    }

    public void setProblem(String problem)
    {
        this.problem = problem;
    }

    public String getSolution()
    {
        return solution;
    }

    public void setSolution(String solution)
    {
        this.solution = solution;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getConfirm()
    {
        return confirm;
    }

    public void setConfirm(String confirm)
    {
        this.confirm = confirm;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getSubmitTimeStr()
    {
        return submitTimeStr;
    }

    public void setSubmitTimeStr(String submitTimeStr)
    {
        this.submitTimeStr = submitTimeStr;
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

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getAppName()
    {
        return appName;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getAppVer()
    {
        return appVer;
    }

    public void setAppVer(String appVer)
    {
        this.appVer = appVer;
    }

    public String getAppVerFmt()
    {
        return appVerFmt;
    }

    public void setAppVerFmt(String appVerFmt)
    {
        this.appVerFmt = appVerFmt;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public String getOsVer()
    {
        return osVer;
    }

    public void setOsVer(String osVer)
    {
        this.osVer = osVer;
    }

    public String getFeedback()
    {
        return feedback;
    }

    public void setFeedback(String feedback)
    {
        this.feedback = feedback;
    }

    public String getImages()
    {
        return images;
    }

    public void setImages(String images)
    {
        this.images = images;
    }

    public String getContack()
    {
        return contack;
    }

    public void setContack(String contack)
    {
        this.contack = contack;
    }

    public Date getSubmitTime()
    {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime)
    {
        this.submitTime = submitTime;
    }

}