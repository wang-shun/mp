package com.fiberhome.mapps.sign.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.sign.entity.Sign;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QuerySignDetailResponse extends BaseResponse
{
    @XmlElement(name = "data")
    private Sign sign;

    public Sign getSign()
    {
        return sign;
    }

    public void setSign(Sign sign)
    {
        this.sign = sign;
    }

}
