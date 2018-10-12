package com.fiberhome.mapps.sign.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.sign.entity.Sign;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QuerySignResponse extends BaseResponse
{
    @XmlElement(name = "data")
    private List<Sign> signList;

    @XmlElement(name = "timestamp")
    private long       timestamp;

    @XmlElement(name = "endflag")
    private int        endflag;

    @XmlElement(name = "total")
    private long       total;

    public List<Sign> getSignList()
    {
        return signList;
    }

    public void setSignList(List<Sign> signList)
    {
        this.signList = signList;
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
