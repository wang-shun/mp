package com.fiberhome.mapps.activity.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.activity.entity.ActivityDetail;
import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryActivityResponse extends BaseResponse
{
    @XmlElement(name = "activity")
    private List<ActivityDetail> getActivityList;

    @XmlElement(name = "timestamp")
    private long                 timestamp;

    @XmlElement(name = "endflag")
    private int                  endflag;

    @XmlElement(name = "total")
    private long                 total;

    public List<ActivityDetail> getGetActivityList()
    {
        return getActivityList;
    }

    public void setGetActivityList(List<ActivityDetail> getActivityList)
    {
        this.getActivityList = getActivityList;
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
