package com.fiberhome.mapps.activity.response;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.activity.entity.AtEnter;
import com.fiberhome.mapps.activity.entity.AtPhoto;
import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryActivityDetailResponse extends BaseResponse
{
    @XmlElement(name = "actId")
    private String        actId;
    @XmlElement(name = "actContent")
    private String        actContent;
    @XmlElement(name = "address")
    private String        address;
    @XmlElement(name = "conTel")
    private String        conTel;
    @XmlElement(name = "numLimit")
    private Integer       numLimit;
    @XmlElement(name = "enterNum")
    private Long          enterNum;
    @XmlElement(name = "imagePath")
    private String        imagePath;
    @XmlElement(name = "phone")
    private String        phone;
    @XmlElement(name = "name")
    private String        name;
    @XmlElement(name = "idCard")
    private String        idCard;
    @XmlElement(name = "remark")
    private String        remark;
    @XmlElement(name = "sex")
    private String        sex;
    @XmlElement(name = "createName")
    private String        createName;
    private String        createId;
    @XmlElement(name = "actStartTime")
    private Date          actStartTime;
    @XmlElement(name = "actEndTime")
    private Date          actEndTime;
    @XmlElement(name = "enterEndTime")
    private Date          enterEndTime;
    @XmlElement(name = "actPosterUrl")
    private String        actPosterUrl;
    @XmlElement(name = "disGroupId")
    private String        disGroupId;
    @XmlElement(name = "actTitle")
    private String        actTitle;
    private String        actStartTimeStr;
    private String        actEndTimeStr;
    private String        enterEndTimeStr;
    @XmlElement(name = "enterList")
    private List<AtEnter> enterList;

    private List<AtPhoto> photoList;

    private String        defaultImage;
    // 0 未参加 1已参加
    private String        joinFlag;
    // 0 不可报名 1可报名
    private int           enterFlag;
    // 0 不是创建人 1是创建人
    private int           createFlag;

    private String        createImAccount;

    public String getCreateImAccount()
    {
        return createImAccount;
    }

    public void setCreateImAccount(String createImAccount)
    {
        this.createImAccount = createImAccount;
    }

    public int getCreateFlag()
    {
        return createFlag;
    }

    public void setCreateFlag(int createFlag)
    {
        this.createFlag = createFlag;
    }

    public String getCreateId()
    {
        return createId;
    }

    public void setCreateId(String createId)
    {
        this.createId = createId;
    }

    public int getEnterFlag()
    {
        return enterFlag;
    }

    public void setEnterFlag(int enterFlag)
    {
        this.enterFlag = enterFlag;
    }

    public String getDefaultImage()
    {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage)
    {
        this.defaultImage = defaultImage;
    }

    public String getJoinFlag()
    {
        return joinFlag;
    }

    public void setJoinFlag(String joinFlag)
    {
        this.joinFlag = joinFlag;
    }

    public String getActId()
    {
        return actId;
    }

    public void setActId(String actId)
    {
        this.actId = actId;
    }

    public String getActContent()
    {
        return actContent;
    }

    public void setActContent(String actContent)
    {
        this.actContent = actContent;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getConTel()
    {
        return conTel;
    }

    public void setConTel(String conTel)
    {
        this.conTel = conTel;
    }

    public Integer getNumLimit()
    {
        return numLimit;
    }

    public void setNumLimit(Integer numLimit)
    {
        this.numLimit = numLimit;
    }

    public Long getEnterNum()
    {
        return enterNum;
    }

    public void setEnterNum(Long enterNum)
    {
        this.enterNum = enterNum;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getCreateName()
    {
        return createName;
    }

    public void setCreateName(String createName)
    {
        this.createName = createName;
    }

    public Date getActStartTime()
    {
        return actStartTime;
    }

    public void setActStartTime(Date actStartTime)
    {
        this.actStartTime = actStartTime;
    }

    public Date getActEndTime()
    {
        return actEndTime;
    }

    public void setActEndTime(Date actEndTime)
    {
        this.actEndTime = actEndTime;
    }

    public Date getEnterEndTime()
    {
        return enterEndTime;
    }

    public void setEnterEndTime(Date enterEndTime)
    {
        this.enterEndTime = enterEndTime;
    }

    public String getActStartTimeStr()
    {
        return actStartTimeStr;
    }

    public void setActStartTimeStr(String actStartTimeStr)
    {
        this.actStartTimeStr = actStartTimeStr;
    }

    public String getActEndTimeStr()
    {
        return actEndTimeStr;
    }

    public void setActEndTimeStr(String actEndTimeStr)
    {
        this.actEndTimeStr = actEndTimeStr;
    }

    public String getEnterEndTimeStr()
    {
        return enterEndTimeStr;
    }

    public void setEnterEndTimeStr(String enterEndTimeStr)
    {
        this.enterEndTimeStr = enterEndTimeStr;
    }

    public String getActPosterUrl()
    {
        return actPosterUrl;
    }

    public void setActPosterUrl(String actPosterUrl)
    {
        this.actPosterUrl = actPosterUrl;
    }

    public String getDisGroupId()
    {
        return disGroupId;
    }

    public void setDisGroupId(String disGroupId)
    {
        this.disGroupId = disGroupId;
    }

    public String getActTitle()
    {
        return actTitle;
    }

    public void setActTitle(String actTitle)
    {
        this.actTitle = actTitle;
    }

    public List<AtEnter> getEnterList()
    {
        return enterList;
    }

    public void setEnterList(List<AtEnter> enterList)
    {
        this.enterList = enterList;
    }

    public List<AtPhoto> getPhotoList()
    {
        return photoList;
    }

    public void setPhotoList(List<AtPhoto> photoList)
    {
        this.photoList = photoList;
    }

}
