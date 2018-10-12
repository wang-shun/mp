package com.fiberhome.mapps.sign.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class QuerySignRequest extends AbstractRopRequest
{
    @NotNull
    private String signDate;
    private String creator;

    private long   timestamp;
    private int    offset;
    private int    limit;

    public String getSignDate()
    {
        return signDate;
    }

    public void setSignDate(String signDate)
    {
        this.signDate = signDate;
    }

    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
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
