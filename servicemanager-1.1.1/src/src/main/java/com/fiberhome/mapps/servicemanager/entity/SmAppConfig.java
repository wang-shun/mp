package com.fiberhome.mapps.servicemanager.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sm_app_config")
public class SmAppConfig {
    @Id
    private String id;

    @Column(name = "app_id")
    private String appId;

    /**
     * 参数key
     */
    @Column(name = "param_key")
    private String paramKey;

    /**
     * 参数值
     */
    @Column(name = "param_value")
    private String paramValue;

    /**
     * 配置版本好，数字，累加
     */
    @Column(name = "config_ver")
    private Long configVer;

    /**
     * 活动为当前有效的配置
     */
    private String actived;

    @Column(name = "setup_user")
    private String setupUser;

    @Column(name = "setup_time")
    private Date setupTime;

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
     * 获取参数key
     *
     * @return param_key - 参数key
     */
    public String getParamKey() {
        return paramKey;
    }

    /**
     * 设置参数key
     *
     * @param paramKey 参数key
     */
    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    /**
     * 获取参数值
     *
     * @return param_value - 参数值
     */
    public String getParamValue() {
        return paramValue;
    }

    /**
     * 设置参数值
     *
     * @param paramValue 参数值
     */
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    /**
     * 获取配置版本好，数字，累加
     *
     * @return config_ver - 配置版本好，数字，累加
     */
    public Long getConfigVer() {
        return configVer;
    }

    /**
     * 设置配置版本好，数字，累加
     *
     * @param configVer 配置版本好，数字，累加
     */
    public void setConfigVer(Long configVer) {
        this.configVer = configVer;
    }

    /**
     * 获取活动为当前有效的配置
     *
     * @return actived - 活动为当前有效的配置
     */
    public String getActived() {
        return actived;
    }

    /**
     * 设置活动为当前有效的配置
     *
     * @param actived 活动为当前有效的配置
     */
    public void setActived(String actived) {
        this.actived = actived;
    }

    /**
     * @return setup_user
     */
    public String getSetupUser() {
        return setupUser;
    }

    /**
     * @param setupUser
     */
    public void setSetupUser(String setupUser) {
        this.setupUser = setupUser;
    }

    /**
     * @return setup_time
     */
    public Date getSetupTime() {
        return setupTime;
    }

    /**
     * @param setupTime
     */
    public void setSetupTime(Date setupTime) {
        this.setupTime = setupTime;
    }
}