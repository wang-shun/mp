package com.fiberhome.mapps.sign.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.sign.entity.Sign;
import com.fiberhome.mapps.sign.entity.StatSign;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryStatResponse extends BaseResponse
{
    @XmlElement(name = "data")
    private StatSign   statSign;

    @XmlElement(name = "rows")
    private List<Sign> list;

    private Long       total;

    public List<Sign> getList()
    {
        return list;
    }

    public void setList(List<Sign> list)
    {
        this.list = list;
    }

    public Long getTotal()
    {
        return total;
    }

    public void setTotal(Long total)
    {
        this.total = total;
    }

    public StatSign getStatSign()
    {
        return statSign;
    }

    public void setStatSign(StatSign statSign)
    {
        this.statSign = statSign;
    }

}
