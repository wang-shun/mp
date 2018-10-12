package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "sm_resource_info")
public class SmResourceInfo {
    @Id
    private String id;

    private String name;

    private String remarks;

    /**
     * 1-数据库,2-redis,3-第三方接入
     */
    private String type;

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
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 获取1-数据库,2-redis,3-第三方接入
     *
     * @return type - 1-数据库,2-redis,3-第三方接入
     */
    public String getType() {
        return type;
    }

    /**
     * 设置1-数据库,2-redis,3-第三方接入
     *
     * @param type 1-数据库,2-redis,3-第三方接入
     */
    public void setType(String type) {
        this.type = type;
    }
}