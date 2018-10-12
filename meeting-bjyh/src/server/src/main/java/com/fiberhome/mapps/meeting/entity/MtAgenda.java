package com.fiberhome.mapps.meeting.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "mt_agenda")
public class MtAgenda
{
    @Id
    private String id;

    @Column(name = "meeting_id")
    private String meetingId;

    @Column(name = "begin_time")
    private Date   beginTime;

    @Column(name = "end_time")
    private Date   endTime;

    private String address;

    private String remarks;
    private String beginTimeStr;
    private String endTimeStr;
    private String dayStr;

    public String getDayStr()
    {
        return dayStr;
    }

    public void setDayStr(String dayStr)
    {
        this.dayStr = dayStr;
    }

    /**
     * @return id
     */
    public String getId()
    {
        return id;
    }

    public String getBeginTimeStr()
    {
        return beginTimeStr;
    }

    public void setBeginTimeStr(String beginTimeStr)
    {
        this.beginTimeStr = beginTimeStr;
    }

    public String getEndTimeStr()
    {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr)
    {
        this.endTimeStr = endTimeStr;
    }

    /**
     * @param id
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return meeting_id
     */
    public String getMeetingId()
    {
        return meetingId;
    }

    /**
     * @param meetingId
     */
    public void setMeetingId(String meetingId)
    {
        this.meetingId = meetingId;
    }

    /**
     * @return begin_time
     */
    public Date getBeginTime()
    {
        return beginTime;
    }

    /**
     * @param beginTime
     */
    public void setBeginTime(Date beginTime)
    {
        this.beginTime = beginTime;
    }

    /**
     * @return end_time
     */
    public Date getEndTime()
    {
        return endTime;
    }

    /**
     * @param endTime
     */
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    /**
     * @return address
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * @return remarks
     */
    public String getRemarks()
    {
        return remarks;
    }

    /**
     * @param remarks
     */
    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }
}