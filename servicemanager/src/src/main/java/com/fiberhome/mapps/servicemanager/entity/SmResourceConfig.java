package com.fiberhome.mapps.servicemanager.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "SM_RESOURCE_CONFIG")
public class SmResourceConfig {
    @Id
    private String id;

    @Column(name = "resource_id")
    private String resourceId;

    /**
     * ����key
     */
    @Column(name = "param_key")
    private String paramKey;

    /**
     * ����ֵ
     */
    @Column(name = "param_value")
    private String paramValue;

    /**
     * ���ð汾�ã����֣��ۼ�
     */
    @Column(name = "config_ver")
    private Long configVer;

    /**
     * �Ϊ��ǰ��Ч������
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
     * @return resource_id
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * @param resourceId
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * ��ȡ����key
     *
     * @return param_key - ����key
     */
    public String getParamKey() {
        return paramKey;
    }

    /**
     * ���ò���key
     *
     * @param paramKey ����key
     */
    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    /**
     * ��ȡ����ֵ
     *
     * @return param_value - ����ֵ
     */
    public String getParamValue() {
        return paramValue;
    }

    /**
     * ���ò���ֵ
     *
     * @param paramValue ����ֵ
     */
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    /**
     * ��ȡ���ð汾�ã����֣��ۼ�
     *
     * @return config_ver - ���ð汾�ã����֣��ۼ�
     */
    public Long getConfigVer() {
        return configVer;
    }

    /**
     * �������ð汾�ã����֣��ۼ�
     *
     * @param configVer ���ð汾�ã����֣��ۼ�
     */
    public void setConfigVer(Long configVer) {
        this.configVer = configVer;
    }

    /**
     * ��ȡ�Ϊ��ǰ��Ч������
     *
     * @return actived - �Ϊ��ǰ��Ч������
     */
    public String getActived() {
        return actived;
    }

    /**
     * ���ûΪ��ǰ��Ч������
     *
     * @param actived �Ϊ��ǰ��Ч������
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