package com.fiberhome.mapps.sign.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class AddSignResponse extends BaseResponse
{
    @XmlElement(name = "signId")
    private String signId;

    public String getSignId()
    {
        return signId;
    }

    public void setSignId(String signId)
    {
        this.signId = signId;
    }

}
