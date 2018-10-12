package com.fiberhome.mapps.meetingroom.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "MR_RES_CONFIG")
public class MrResConfig
{
    @Id
    private String id;

    @Column(name = "res_name")
    private String resName;

    @Column(name = "res_type")
    private String resType;

    @Column(name = "res_user_id")
    private String resUserId;

    @Column(name = "res_order")
    private Long   resOrder;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getResName()
    {
        return resName;
    }

    public void setResName(String resName)
    {
        this.resName = resName;
    }

    public String getResType()
    {
        return resType;
    }

    public void setResType(String resType)
    {
        this.resType = resType;
    }

    public String getResUserId()
    {
        return resUserId;
    }

    public void setResUserId(String resUserId)
    {
        this.resUserId = resUserId;
    }

    public Long getResOrder()
    {
        return resOrder;
    }

    public void setResOrder(Long resOrder)
    {
        this.resOrder = resOrder;
    }

}