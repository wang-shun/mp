package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "sm_app_metadata")
public class SmAppMetadata {
    @Id
    @Column(name = "app_id")
    private String appId;

    /**
     * json
     */
    private String metadata;

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
     * 获取json
     *
     * @return metadata - json
     */
    public String getMetadata() {
        return metadata;
    }

    /**
     * 设置json
     *
     * @param metadata json
     */
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}