package com.fiberhome.mapps.meetingroom.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "MR_RESERVED_RES")
public class MrReservedRes
{
    @Id
    private String id;

    @Column(name = "res_id")
    private String resId;

    @Column(name = "reserved_id")
    private String reservedId;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getResId()
    {
        return resId;
    }

    public void setResId(String resId)
    {
        this.resId = resId;
    }

    public String getReservedId()
    {
        return reservedId;
    }

    public void setReservedId(String reservedId)
    {
        this.reservedId = reservedId;
    }

}