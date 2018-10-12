package com.fiberhome.mapps.activity.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "AT_ACTIVITY")
public class AtActivity
{
    @Id
    private String  id;

    private String  ecid;

    @Column(name = "act_content")
    private String  actContent;

    @Column(name = "act_start_time")
    private Date    actStartTime;

    @Column(name = "act_end_time")
    private Date    actEndTime;

    @Column(name = "act_address")
    private String  actAddress;

    @Column(name = "act_coordinate")
    private String  actCoordinate;

    @Column(name = "enter_end_time")
    private Date    enterEndTime;

    @Column(name = "con_tel")
    private String  conTel;

    @Column(name = "num_limit")
    private Integer numLimit;

    @Column(name = "enter_num")
    private Long    enterNum;

    @Column(name = "act_title")
    private String  actTitle;

    private String  phone;

    private String  name;

    @Column(name = "id_card")
    private String  idCard;

    private String  remark;

    private String  sex;

    @Column(name = "create_time")
    private Date    createTime;

    @Column(name = "modified_time")
    private Date    modifiedTime;

    @Column(name = "create_name")
    private String  createName;

    @Column(name = "create_id")
    private String  createId;

    @Column(name = "act_poster_url")
    private String  actPosterUrl;

    @Column(name = "dis_group_id")
    private String  disGroupId;

    @Column(name = "default_image")
    private String  defaultImage;

    public String getDefaultImage()
    {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage)
    {
        this.defaultImage = defaultImage;
    }

    public String getEcid()
    {
        return ecid;
    }

    public void setEcid(String ecid)
    {
        this.ecid = ecid;
    }

    /**
     * @return id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return act_content
     */
    public String getActContent()
    {
        return actContent;
    }

    /**
     * @param actContent
     */
    public void setActContent(String actContent)
    {
        this.actContent = actContent;
    }

    /**
     * @return act_start_time
     */
    public Date getActStartTime()
    {
        return actStartTime;
    }

    /**
     * @param actStartTime
     */
    public void setActStartTime(Date actStartTime)
    {
        this.actStartTime = actStartTime;
    }

    /**
     * @return act_end_time
     */
    public Date getActEndTime()
    {
        return actEndTime;
    }

    /**
     * @param actEndTime
     */
    public void setActEndTime(Date actEndTime)
    {
        this.actEndTime = actEndTime;
    }

    /**
     * @return act_address
     */
    public String getActAddress()
    {
        return actAddress;
    }

    /**
     * @param actAddress
     */
    public void setActAddress(String actAddress)
    {
        this.actAddress = actAddress;
    }

    /**
     * @return act_coordinate
     */
    public String getActCoordinate()
    {
        return actCoordinate;
    }

    /**
     * @param actCoordinate
     */
    public void setActCoordinate(String actCoordinate)
    {
        this.actCoordinate = actCoordinate;
    }

    /**
     * @return enter_end_time
     */
    public Date getEnterEndTime()
    {
        return enterEndTime;
    }

    /**
     * @param enterEndTime
     */
    public void setEnterEndTime(Date enterEndTime)
    {
        this.enterEndTime = enterEndTime;
    }

    /**
     * @return con_tel
     */
    public String getConTel()
    {
        return conTel;
    }

    /**
     * @param conTel
     */
    public void setConTel(String conTel)
    {
        this.conTel = conTel;
    }

    /**
     * @return num_limit
     */
    public Integer getNumLimit()
    {
        return numLimit;
    }

    /**
     * @param numLimit
     */
    public void setNumLimit(Integer numLimit)
    {
        this.numLimit = numLimit;
    }

    /**
     * @return enter_num
     */
    public Long getEnterNum()
    {
        return enterNum;
    }

    /**
     * @param enterNum
     */
    public void setEnterNum(Long enterNum)
    {
        this.enterNum = enterNum;
    }

    /**
     * ��ȡ�Ƿ��У�1 ��/0 ��
     *
     * @return phone - �Ƿ��У�1 ��/0 ��
     */
    public String getPhone()
    {
        return phone;
    }

    /**
     * �����Ƿ��У�1 ��/0 ��
     *
     * @param phone �Ƿ��У�1 ��/0 ��
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    /**
     * ��ȡ�Ƿ��У�1 ��/0 ��
     *
     * @return name - �Ƿ��У�1 ��/0 ��
     */
    public String getName()
    {
        return name;
    }

    /**
     * �����Ƿ��У�1 ��/0 ��
     *
     * @param name �Ƿ��У�1 ��/0 ��
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * ��ȡ�Ƿ��У�1 ��/0 ��
     *
     * @return id_card - �Ƿ��У�1 ��/0 ��
     */
    public String getIdCard()
    {
        return idCard;
    }

    /**
     * �����Ƿ��У�1 ��/0 ��
     *
     * @param idCard �Ƿ��У�1 ��/0 ��
     */
    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    /**
     * ��ȡ�Ƿ��У�1 ��/0 ��
     *
     * @return remark - �Ƿ��У�1 ��/0 ��
     */
    public String getRemark()
    {
        return remark;
    }

    /**
     * �����Ƿ��У�1 ��/0 ��
     *
     * @param remark �Ƿ��У�1 ��/0 ��
     */
    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    /**
     * ��ȡ�Ƿ��У�1 ��/0 ��
     *
     * @return sex - �Ƿ��У�1 ��/0 ��
     */
    public String getSex()
    {
        return sex;
    }

    /**
     * �����Ƿ��У�1 ��/0 ��
     *
     * @param sex �Ƿ��У�1 ��/0 ��
     */
    public void setSex(String sex)
    {
        this.sex = sex;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime()
    {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    /**
     * @return modified_time
     */
    public Date getModifiedTime()
    {
        return modifiedTime;
    }

    /**
     * @param modifiedTime
     */
    public void setModifiedTime(Date modifiedTime)
    {
        this.modifiedTime = modifiedTime;
    }

    /**
     * @return create_name
     */
    public String getCreateName()
    {
        return createName;
    }

    /**
     * @param createName
     */
    public void setCreateName(String createName)
    {
        this.createName = createName;
    }

    /**
     * @return create_id
     */
    public String getCreateId()
    {
        return createId;
    }

    /**
     * @param createId
     */
    public void setCreateId(String createId)
    {
        this.createId = createId;
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

}