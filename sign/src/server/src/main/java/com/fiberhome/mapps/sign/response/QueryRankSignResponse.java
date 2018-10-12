package com.fiberhome.mapps.sign.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.sign.entity.Sign;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryRankSignResponse extends BaseResponse
{
    @XmlElement(name = "data")
    private Sign signInfo;

    public Sign getSignInfo()
    {
        return signInfo;
    }

    public void setSignInfo(Sign signInfo)
    {
        this.signInfo = signInfo;
    }

}
