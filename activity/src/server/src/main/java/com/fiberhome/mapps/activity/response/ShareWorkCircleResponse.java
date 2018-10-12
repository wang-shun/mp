package com.fiberhome.mapps.activity.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class ShareWorkCircleResponse extends BaseResponse
{
    @XmlElement(name = "comment")
    private String  comment;
    @XmlElement(name = "title")
    private String  title;
    @XmlElement(name = "desc")
    private String  desc;
    @XmlElement(name = "appname")
    private String  appname;
    @XmlElement(name = "appid")
    private String  appid;
    @XmlElement(name = "imgUrl")
    private String  imgUrl;
    @XmlElement(name = "link")
    private String  link;
    @XmlElement(name = "id")
    private String  id;
    @XmlElement(name = "imFlag")
    private boolean imFlag;
    @XmlElement(name = "phone")
    private String  phone;

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public boolean isImFlag()
    {
        return imFlag;
    }

    public void setImFlag(boolean imFlag)
    {
        this.imFlag = imFlag;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getAppname()
    {
        return appname;
    }

    public void setAppname(String appname)
    {
        this.appname = appname;
    }

    public String getAppid()
    {
        return appid;
    }

    public void setAppid(String appid)
    {
        this.appid = appid;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
