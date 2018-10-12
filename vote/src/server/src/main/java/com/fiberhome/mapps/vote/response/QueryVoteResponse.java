package com.fiberhome.mapps.vote.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.vote.entity.VoteInfo;
import com.fiberhome.mapps.vote.entity.VoteItemInfo;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryVoteResponse extends BaseResponse
{
    @XmlElement(name = "vote")
    private List<VoteInfo>     voteList;

    @XmlElement(name = "voteInfo")
    private VoteInfo           voteInfo;

    @XmlElement(name = "items")
    private List<VoteItemInfo> voteItems;

    @XmlElement(name = "timestamp")
    private long               timestamp;

    @XmlElement(name = "endflag")
    private int                endflag;

    @XmlElement(name = "total")
    private long               total;
    @XmlElement(name = "data")
    private Object             data;

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public VoteInfo getVoteInfo()
    {
        return voteInfo;
    }

    public void setVoteInfo(VoteInfo voteInfo)
    {
        this.voteInfo = voteInfo;
    }

    public List<VoteItemInfo> getVoteItems()
    {
        return voteItems;
    }

    public void setVoteItems(List<VoteItemInfo> voteItems)
    {
        this.voteItems = voteItems;
    }

    public List<VoteInfo> getVoteList()
    {
        return voteList;
    }

    public void setVoteList(List<VoteInfo> voteList)
    {
        this.voteList = voteList;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public int getEndflag()
    {
        return endflag;
    }

    public void setEndflag(int endflag)
    {
        this.endflag = endflag;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

}
