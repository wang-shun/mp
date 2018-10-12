package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "SM_RESOURCE_INFO")
public class SmResourceInfo {
    @Id
    private String id;

    private String name;

    private String remarks;

    /**
     * 1-���ݿ�,2-redis,3-����������
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
     * ��ȡ1-���ݿ�,2-redis,3-����������
     *
     * @return type - 1-���ݿ�,2-redis,3-����������
     */
    public String getType() {
        return type;
    }

    /**
     * ����1-���ݿ�,2-redis,3-����������
     *
     * @param type 1-���ݿ�,2-redis,3-����������
     */
    public void setType(String type) {
        this.type = type;
    }
}