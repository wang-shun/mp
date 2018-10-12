package com.fiberhome.mapps.meeting.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tmp_reserved")
public class TmpReserved
{
    @Id
    private String id;

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "time_begin")
    private Date   timeBegin;

    @Column(name = "time_end")
    private Date   timeEnd;

    @Column(name = "create_time")
    private Date   createTime;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

    public Date getTimeBegin()
    {
        return timeBegin;
    }

    public void setTimeBegin(Date timeBegin)
    {
        this.timeBegin = timeBegin;
    }

    public Date getTimeEnd()
    {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd)
    {
        this.timeEnd = timeEnd;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

}