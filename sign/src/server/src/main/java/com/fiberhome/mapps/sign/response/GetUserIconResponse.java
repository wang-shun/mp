package com.fiberhome.mapps.sign.response;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class GetUserIconResponse extends BaseResponse
{
    @XmlElement(name = "data")
    private HashMap<String, String> uim;

    public HashMap<String, String> getUim()
    {
        return uim;
    }

    public void setUim(HashMap<String, String> uim)
    {
        this.uim = uim;
    }

}
