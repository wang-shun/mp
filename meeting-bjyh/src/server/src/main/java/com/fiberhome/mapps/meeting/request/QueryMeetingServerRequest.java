package com.fiberhome.mapps.meeting.request;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.rop.AbstractRopRequest;

public class QueryMeetingServerRequest extends AbstractRopRequest
{

    @Size(max = 30, message = "会议名称最长30个字")
    private String meetingName;
    private String beginTime;
    private String endTime;
    private String create_beginTime;
    private String create_endTime;
    @Size(max = 30, message = "会议地点最长30个字")
    private String address;
    private String meetingHolder;
    @Pattern(regexp = "0|10|20|30|40|50", message = "格式不合法")
    private String meetingStatus;
    @Pattern(regexp = "1|2", message = "格式不合法")
    private String order;
    private int    offset;
    private int    limit;
    private String sort;

    private String selfStatus;

    public String getMeetingName()
    {
        return meetingName;
    }

    public void setMeetingName(String meetingName)
    {
        this.meetingName = meetingName;
    }

    public String getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(String beginTime)
    {
        this.beginTime = beginTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getMeetingHolder()
    {
        return meetingHolder;
    }

    public void setMeetingHolder(String meetingHolder)
    {
        this.meetingHolder = meetingHolder;
    }

    public String getMeetingStatus()
    {
        return meetingStatus;
    }

    public void setMeetingStatus(String meetingStatus)
    {
        this.meetingStatus = meetingStatus;
    }

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }

    public String getCreate_beginTime()
    {
        return create_beginTime;
    }

    public void setCreate_beginTime(String create_beginTime)
    {
        this.create_beginTime = create_beginTime;
    }

    public String getCreate_endTime()
    {
        return create_endTime;
    }

    public void setCreate_endTime(String create_endTime)
    {
        this.create_endTime = create_endTime;
    }

    public int getOffset()
    {
        return offset;
    }

    public void setOffset(int offset)
    {
        this.offset = offset;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    public String getSort()
    {
        return sort;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
    }

    public String getSelfStatus()
    {
        return selfStatus;
    }

    public void setSelfStatus(String selfStatus)
    {
        this.selfStatus = selfStatus;
    }

}
