package com.fiberhome.mapps.servicemanager.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "SM_RESOURCE_ASSIGN")
public class SmResourceAssign {
    @Id
    private String id;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "app_name")
    private String appName;

    /**
     * ��������ԴӦ��ID
     */
    @Column(name = "res_id")
    private String resId;

    /**
     * ʶ����Դ�Ĵ��룬����ĳ��Ӧ�ÿ�������ͬһ��Դ���͵Ķ��ʵ����Ĭ��Ϊdefault
     */
    @Column(name = "res_code")
    private String resCode;

    @Column(name = "res_name")
    private String resName;

    @Column(name = "assign_time")
    private Date assignTime;

    @Column(name = "reource_id")
    private String reourceId;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return app_id
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return app_name
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * ��ȡ��������ԴӦ��ID
     *
     * @return res_id - ��������ԴӦ��ID
     */
    public String getResId() {
        return resId;
    }

    /**
     * ������������ԴӦ��ID
     *
     * @param resId ��������ԴӦ��ID
     */
    public void setResId(String resId) {
        this.resId = resId;
    }

    /**
     * ��ȡʶ����Դ�Ĵ��룬����ĳ��Ӧ�ÿ�������ͬһ��Դ���͵Ķ��ʵ����Ĭ��Ϊdefault
     *
     * @return res_code - ʶ����Դ�Ĵ��룬����ĳ��Ӧ�ÿ�������ͬһ��Դ���͵Ķ��ʵ����Ĭ��Ϊdefault
     */
    public String getResCode() {
        return resCode;
    }

    /**
     * ����ʶ����Դ�Ĵ��룬����ĳ��Ӧ�ÿ�������ͬһ��Դ���͵Ķ��ʵ����Ĭ��Ϊdefault
     *
     * @param resCode ʶ����Դ�Ĵ��룬����ĳ��Ӧ�ÿ�������ͬһ��Դ���͵Ķ��ʵ����Ĭ��Ϊdefault
     */
    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    /**
     * @return res_name
     */
    public String getResName() {
        return resName;
    }

    /**
     * @param resName
     */
    public void setResName(String resName) {
        this.resName = resName;
    }

    /**
     * @return assign_time
     */
    public Date getAssignTime() {
        return assignTime;
    }

    /**
     * @param assignTime
     */
    public void setAssignTime(Date assignTime) {
        this.assignTime = assignTime;
    }

    /**
     * @return reource_id
     */
    public String getReourceId() {
        return reourceId;
    }

    /**
     * @param reourceId
     */
    public void setReourceId(String reourceId) {
        this.reourceId = reourceId;
    }
}