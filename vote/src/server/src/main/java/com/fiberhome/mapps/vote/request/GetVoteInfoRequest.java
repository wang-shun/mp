package com.fiberhome.mapps.vote.request;

import com.rop.AbstractRopRequest;

public class GetVoteInfoRequest extends AbstractRopRequest
{
    private String  id;

    private String  itemIds;

    private boolean inc = false;

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
}
