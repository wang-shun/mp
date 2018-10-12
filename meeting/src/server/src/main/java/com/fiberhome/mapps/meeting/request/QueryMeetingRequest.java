package com.fiberhome.mapps.meeting.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.rop.AbstractRopRequest;

public class QueryMeetingRequest extends AbstractRopRequest
{
    @Size(max = 30, message = "会议室名称最长30个字")
    private String meetingName;
    @Size(max = 100, message = "会议室地址最长100个字")
    private String meetingStatus;
    @Pattern(regexp = "1|2|3", message = "格式不合法")
    private String selfStatus;
    @NotNull
    private long   timestamp;
    @NotNull
    @Min(value = 1, message = "分页页数最小为1")
    private int    offset;
    @NotNull
    @Min(value = 1, message = "每页记录数最小为1")
    @Max(value = 100, message = "每页记录数最大为100")
    private int    limit;

    private String selectDate;
    private String loginUserFlag;

    public String getSelectDate()
    {
        return selectDate;
    }

    public void setSelectDate(String selectDate)
    {
        this.selectDate = selectDate;
    }

    public String getLoginUserFlag()
    {
        return loginUserFlag;
    }

    public void setLoginUserFlag(String loginUserFlag)
    {
        this.loginUserFlag = loginUserFlag;
    }

    public String getMeetingName()
    {
        return meetingName;
    }

    public void setMeetingName(String meetingName)
    {
        this.meetingName = meetingName;
    }

    public String getMeetingStatus()
    {
        return meetingStatus;
    }

    public void setMeetingStatus(String meetingStatus)
    {
        this.meetingStatus = meetingStatus;
    }

    public String getSelfStatus()
    {
        return selfStatus;
    }

    public void setSelfStatus(String selfStatus)
    {
        this.selfStatus = selfStatus;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
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

}
