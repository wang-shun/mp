package com.fiberhome.mapps.meeting.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class FileInfoResponse extends BaseResponse
{
    @XmlElement
    private String path;

    @XmlElement
    private String url;

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}
