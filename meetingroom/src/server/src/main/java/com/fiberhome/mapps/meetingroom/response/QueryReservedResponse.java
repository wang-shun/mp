package com.fiberhome.mapps.meetingroom.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.meetingroom.entity.GetMrReserved;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryReservedResponse extends BaseResponse
{
    @XmlElement(name = "reserved")
    private List<GetMrReserved> mrReservedList;

    @XmlElement(name = "timestamp")
    private long                timestamp;

    @XmlElement(name = "endflag")
    private int                 endflag;

    @XmlElement(name = "total")
    private long                total;

    public List<GetMrReserved> getMrReservedList()
    {
        return mrReservedList;
    }

    public void setMrReservedList(List<GetMrReserved> mrReservedList)
    {
        this.mrReservedList = mrReservedList;
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
