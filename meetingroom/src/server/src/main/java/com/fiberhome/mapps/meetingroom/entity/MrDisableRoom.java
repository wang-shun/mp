package com.fiberhome.mapps.meetingroom.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "MR_DISABLE_ROOM")
public class MrDisableRoom
{
    @Id
    private String id;

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "disable_begin_time")
    private String disableBeginTime;

    @Column(name = "disable_end_time")
    private String disableEndTime;

    @Column(name = "disable_user_id")
    private String disableUserId;

    @Column(name = "disable_user_name")
    private String disableUserName;

    @Column(name = "disable_user_dept")
    private String disableUserDept;

    @Column(name = "status")
    private String status;

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

    public String getDisableBeginTime()
    {
        return disableBeginTime;
    }

    public void setDisableBeginTime(String disableBeginTime)
    {
        this.disableBeginTime = disableBeginTime;
    }

    public String getDisableEndTime()
    {
        return disableEndTime;
    }

    public void setDisableEndTime(String disableEndTime)
    {
        this.disableEndTime = disableEndTime;
    }

    public String getDisableUserId()
    {
        return disableUserId;
    }

    public void setDisableUserId(String disableUserId)
    {
        this.disableUserId = disableUserId;
    }

    public String getDisableUserName()
    {
        return disableUserName;
    }

    public void setDisableUserName(String disableUserName)
    {
        this.disableUserName = disableUserName;
    }

    public String getDisableUserDept()
    {
        return disableUserDept;
    }

    public void setDisableUserDept(String disableUserDept)
    {
        this.disableUserDept = disableUserDept;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}