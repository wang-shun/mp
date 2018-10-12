package com.fiberhome.mapps.trip.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.FdFeedback;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryFeedbackResponse extends BaseResponse
{
    @XmlElement(name = "feedbackList")
    private List<FdFeedback> list;

    @XmlElement(name = "total")
    private long             total;

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public List<FdFeedback> getList()
    {
        return list;
    }

    public void setList(List<FdFeedback> list)
    {
        this.list = list;
    }

}
