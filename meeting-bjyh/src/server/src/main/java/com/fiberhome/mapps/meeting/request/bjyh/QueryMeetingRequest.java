package com.fiberhome.mapps.meeting.request.bjyh;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class QueryMeetingRequest extends AbstractRopRequest
{
    private String meetingName;
    private String selectDate;
    @NotNull
    private long   timestamp;
    @NotNull
    @Min(value = 1, message = "分页页数最小为1")
    private int    offset;
    @NotNull
    @Min(value = 1, message = "每页记录数最小为1")
    @Max(value = 100, message = "每页记录数最大为100")
    private int    limit;

    private String loginUserFlag;

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

    public String getSelectDate()
    {
        return selectDate;
    }

    public void setSelectDate(String selectDate)
    {
        this.selectDate = selectDate;
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
