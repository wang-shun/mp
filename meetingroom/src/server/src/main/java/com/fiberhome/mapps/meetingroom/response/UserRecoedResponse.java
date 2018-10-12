package com.fiberhome.mapps.meetingroom.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.meetingroom.entity.GetOpLog;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class UserRecoedResponse extends BaseResponse
{
    @XmlElement(name = "opLogList")
    private List<GetOpLog> opLogList;
    @XmlElement(name = "total")
    private long           total;

    public List<GetOpLog> getOpLogList()
    {
        return opLogList;
    }

    public void setOpLogList(List<GetOpLog> opLogList)
    {
        this.opLogList = opLogList;
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
