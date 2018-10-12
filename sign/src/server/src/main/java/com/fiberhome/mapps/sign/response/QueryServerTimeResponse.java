package com.fiberhome.mapps.sign.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryServerTimeResponse extends BaseResponse
{
    @XmlElement(name = "data")
    private String formatTime;

    public String getFormatTime()
    {
        return formatTime;
    }

    public void setFormatTime(String formatTime)
    {
        this.formatTime = formatTime;
    }

}
