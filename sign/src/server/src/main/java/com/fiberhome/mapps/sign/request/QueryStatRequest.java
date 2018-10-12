package com.fiberhome.mapps.sign.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class QueryStatRequest extends AbstractRopRequest
{
    @NotNull
    private String signDate;
    private String depIds;
    private String userIds;
    private String virtualGroupIds;

    private int    offset;

    private int    limit;

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

    public String getSignDate()
    {
        return signDate;
    }

    public void setSignDate(String signDate)
    {
        this.signDate = signDate;
    }

    public String getDepIds()
    {
        return depIds;
    }

    public void setDepIds(String depIds)
    {
        this.depIds = depIds;
    }

    public String getUserIds()
    {
        return userIds;
    }

    public void setUserIds(String userIds)
    {
        this.userIds = userIds;
    }

    public String getVirtualGroupIds()
    {
        return virtualGroupIds;
    }

    public void setVirtualGroupIds(String virtualGroupIds)
    {
        this.virtualGroupIds = virtualGroupIds;
    }
}
