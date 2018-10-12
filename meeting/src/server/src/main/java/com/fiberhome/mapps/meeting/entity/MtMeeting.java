package com.fiberhome.mapps.meeting.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fiberhome.mapps.intergration.mybatis.OrgDept;

@OrgDept(orgDepId = "orgDeptId", orgDeptOrder = "orgDeptOrder")
@Table(name = "MT_MEETING")
public class MtMeeting
{
    @Id
    private String  id;

    private String  ecid;

    private String  name;

    private String  address;

    @Column(name = "begin_time")
    private Date    beginTime;

    @Column(name = "end_time")
    private Date    endTime;

    private String  sponsor;

    @Column(name = "create_time")
    private Date    createTime;

    /**
     * 草稿：10 未进行：20 进行中：30 已取消：40 已结束：50
     */
    private String  status;

    @Column(name = "release_time")
    private Date    releaseTime;

    @Column(name = "has_group")
    private String  hasGroup;

    @Column(name = "group_id")
    private String  groupId;

    @Column(name = "org_dept_id")
    private String  orgDeptId;    // 所属部门机构id
    @Column(name = "org_dept_order")
    private String  orgDeptOrder; // 所属部门机构order

    private String  qrcode;

    @Column(name = "notice_type")
    private String  noticeType;

    @Column(name = "notice_set")
    private Integer noticeSet;

    @Column(name = "notice_time")
    private Date    noticeTime;

    @Column(name = "sign_type")
    private String  signType;

    public String getQrcode()
    {
        return qrcode;
    }

    public void setQrcode(String qrcode)
    {
        this.qrcode = qrcode;
    }

    public String getNoticeType()
    {
        return noticeType;
    }

    public void setNoticeType(String noticeType)
    {
        this.noticeType = noticeType;
    }

    public Integer getNoticeSet()
    {
        return noticeSet;
    }

    public void setNoticeSet(Integer noticeSet)
    {
        this.noticeSet = noticeSet;
    }

    public Date getNoticeTime()
    {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime)
    {
        this.noticeTime = noticeTime;
    }

    public String getSignType()
    {
        return signType;
    }

    public void setSignType(String signType)
    {
        this.signType = signType;
    }

    public String getOrgDeptId()
    {
        return orgDeptId;
    }

    public void setOrgDeptId(String orgDeptId)
    {
        this.orgDeptId = orgDeptId;
    }

    public String getOrgDeptOrder()
    {
        return orgDeptOrder;
    }

    public void setOrgDeptOrder(String orgDeptOrder)
    {
        this.orgDeptOrder = orgDeptOrder;
    }

    public String getHasGroup()
    {
        return hasGroup;
    }

    public void setHasGroup(String hasGroup)
    {
        this.hasGroup = hasGroup;
    }

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
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
     * @return ecid
     */
    public String getEcid()
    {
        return ecid;
    }

    /**
     * @param ecid
     */
    public void setEcid(String ecid)
    {
        this.ecid = ecid;
    }

    /**
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return address
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * @return begin_time
     */
    public Date getBeginTime()
    {
        return beginTime;
    }

    /**
     * @param beginTime
     */
    public void setBeginTime(Date beginTime)
    {
        this.beginTime = beginTime;
    }

    /**
     * @return end_time
     */
    public Date getEndTime()
    {
        return endTime;
    }

    /**
     * @param endTime
     */
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    /**
     * @return sponsor
     */
    public String getSponsor()
    {
        return sponsor;
    }

    /**
     * @param sponsor
     */
    public void setSponsor(String sponsor)
    {
        this.sponsor = sponsor;
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
     * 获取草稿：10 未进行：20 进行中：30 已取消：40 已结束：50
     *
     * @return status - 草稿：10 未进行：20 进行中：30 已取消：40 已结束：50
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * 设置草稿：10 未进行：20 进行中：30 已取消：40 已结束：50
     *
     * @param status 草稿：10 未进行：20 进行中：30 已取消：40 已结束：50
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * @return release_time
     */
    public Date getReleaseTime()
    {
        return releaseTime;
    }

    /**
     * @param releaseTime
     */
    public void setReleaseTime(Date releaseTime)
    {
        this.releaseTime = releaseTime;
    }
}