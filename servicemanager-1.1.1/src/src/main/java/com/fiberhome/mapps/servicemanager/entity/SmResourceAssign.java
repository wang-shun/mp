package com.fiberhome.mapps.servicemanager.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sm_resource_assign")
public class SmResourceAssign {
    @Id
    private String id;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "app_name")
    private String appName;

    /**
     * 依赖的资源应用ID
     */
    @Column(name = "res_id")
    private String resId;

    /**
     * 识别资源的代码，比如某个应用可能依赖同一资源类型的多个实例，默认为default
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
     * 获取依赖的资源应用ID
     *
     * @return res_id - 依赖的资源应用ID
     */
    public String getResId() {
        return resId;
    }

    /**
     * 设置依赖的资源应用ID
     *
     * @param resId 依赖的资源应用ID
     */
    public void setResId(String resId) {
        this.resId = resId;
    }

    /**
     * 获取识别资源的代码，比如某个应用可能依赖同一资源类型的多个实例，默认为default
     *
     * @return res_code - 识别资源的代码，比如某个应用可能依赖同一资源类型的多个实例，默认为default
     */
    public String getResCode() {
        return resCode;
    }

    /**
     * 设置识别资源的代码，比如某个应用可能依赖同一资源类型的多个实例，默认为default
     *
     * @param resCode 识别资源的代码，比如某个应用可能依赖同一资源类型的多个实例，默认为default
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