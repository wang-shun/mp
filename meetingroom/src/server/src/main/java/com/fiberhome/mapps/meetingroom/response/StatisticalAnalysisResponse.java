package com.fiberhome.mapps.meetingroom.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.meetingroom.entity.StatisticalAnalysis;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class StatisticalAnalysisResponse extends BaseResponse
{
    @XmlElement(name = "statList")
    private List<StatisticalAnalysis> statList;
    @XmlElement(name = "total")
    private long                      total;

    public List<StatisticalAnalysis> getStatList()
    {
        return statList;
    }

    public void setStatList(List<StatisticalAnalysis> statList)
    {
        this.statList = statList;
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
