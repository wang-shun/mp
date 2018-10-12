package com.fiberhome.mapps.vote.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class QueryVoteRequest extends AbstractRopRequest
{
    private String  id;
    private String  reqSource;

    private String  title;
    @NotNull
    private long    timestamp;
    @NotNull
    @Min(value = 1, message = "分页页数最小为1")
    private int     offset;
    @NotNull
    @Min(value = 1, message = "每页记录数最小为1")
    @Max(value = 100, message = "每页记录数最大为100")
    private int     limit;

    private boolean inc = false;
    private String  itemIds;
    private String  depIds;

    public String getDepIds()
    {
        return depIds;
    }

    public void setDepIds(String depIds)
    {
        this.depIds = depIds;
    }

    public String getItemIds()
    {
        return itemIds;
    }

    public void setItemIds(String itemIds)
    {
        this.itemIds = itemIds;
    }

    public boolean isInc()
    {
        return inc;
    }

    public void setInc(boolean inc)
    {
        this.inc = inc;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getReqSource()
    {
        return reqSource;
    }

    public void setReqSource(String reqSource)
    {
        this.reqSource = reqSource;
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
