package com.fiberhome.mapps.meeting.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class QueryMeetingDetailRequest extends AbstractRopRequest
{
    @NotNull
    private String  meetingId;

    private String  sequId;
    private long    timestamp;
    private int     offset;

    private int     limit;
    private boolean refreshFlag;
    
    private String sort;

    public boolean isRefreshFlag()
    {
        return refreshFlag;
    }

    public void setRefreshFlag(boolean refreshFlag)
    {
        this.refreshFlag = refreshFlag;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getSequId()
    {
        return sequId;
    }

    public void setSequId(String sequId)
    {
        this.sequId = sequId;
    }

    public String getMeetingId()
    {
        return meetingId;
    }

    public void setMeetingId(String meetingId)
    {
        this.meetingId = meetingId;
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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}
