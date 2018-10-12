package com.fiberhome.mapps.vote.request;

import java.util.List;

import com.fiberhome.mapps.vote.entity.VoteInfo;
import com.fiberhome.mapps.vote.entity.VtVoteItem;
import com.rop.AbstractRopRequest;

public class SaveVoteRequest extends AbstractRopRequest
{
    private String           reqSource;
    private String           depIds;
    private String           userIds;

    private List<VtVoteItem> voteItems;
    private VoteInfo         voteInfo;

    public String getReqSource()
    {
        return reqSource;
    }

    public void setReqSource(String reqSource)
    {
        this.reqSource = reqSource;
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

    public List<VtVoteItem> getVoteItems()
    {
        return voteItems;
    }

    public void setVoteItems(List<VtVoteItem> voteItems)
    {
        this.voteItems = voteItems;
    }

    public VoteInfo getVoteInfo()
    {
        return voteInfo;
    }

    public void setVoteInfo(VoteInfo voteInfo)
    {
        this.voteInfo = voteInfo;
    }
}
