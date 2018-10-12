package com.fiberhome.mapps.sign.entity;

import java.util.Date;
import java.util.List;

import com.fiberhome.mapps.sign.utils.DateUtil;

public class Sign
{
    private String       signId;

    /**
     * 原应用中为org_id
     */
    private String       ecid;

    private Date         signTime;
    private String       signTimeStr;

    private String       longitude;

    private String       latitude;

    private String       address;

    private String       city;

    private String       content;

    private String       creator;

    private String       creatorName;

    private Date         createTime;
    private String       createTimeStr;

    private String       depId;

    /**
     * 记录状态,1有效,0无效,默认1,当前无用
     */
    private String       state;

    private String       creatorIcon;  // 创建人头像
    private String       orgId;        // 所属机构
    private Integer      signCount;    // 人员签到计数
    private String       signDate;     // 签到日期
    private List<String> images;       // 签到图片列表
    private String       taskNo;       // 签到任务
    private String       manhour;      // 任务工时
    private String       creatorNum;   // 创建者名称
    private String       deptOrder;
    
    private String       orgDepId;     //所属部门机构id
    private String       orgDeptOrder; //所属部门机构order
    
    
    public String getOrgDepId() {
		return orgDepId;
	}

	public void setOrgDepId(String orgDepId) {
		this.orgDepId = orgDepId;
	}

	public String getOrgDeptOrder() {
		return orgDeptOrder;
	}

	public void setOrgDeptOrder(String orgDeptOrder) {
		this.orgDeptOrder = orgDeptOrder;
	}

    public String getDeptOrder()
    {
        return deptOrder;
    }

    public void setDeptOrder(String deptOrder)
    {
        this.deptOrder = deptOrder;
    }

    public String getCreatorNum()
    {
        return creatorNum;
    }

    public void setCreatorNum(String creatorNum)
    {
        this.creatorNum = creatorNum;
    }

    public String getTaskNo()
    {
        return taskNo;
    }

    public void setTaskNo(String taskNo)
    {
        this.taskNo = taskNo;
    }

    public String getManhour()
    {
        return manhour;
    }

    public void setManhour(String manhour)
    {
        this.manhour = manhour;
    }

    public String getSignTimeStr()
    {
        return signTimeStr;
    }

    public void setSignTimeStr(String signTimeStr)
    {
        this.signTimeStr = signTimeStr;
    }

    public String getCreateTimeStr()
    {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr)
    {
        this.createTimeStr = createTimeStr;
    }

    public String getCreatorIcon()
    {
        return creatorIcon;
    }

    public void setCreatorIcon(String creatorIcon)
    {
        this.creatorIcon = creatorIcon;
    }

    public String getOrgId()
    {
        return orgId;
    }

    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
    }

    public Integer getSignCount()
    {
        return signCount;
    }

    public void setSignCount(Integer signCount)
    {
        this.signCount = signCount;
    }

    public String getSignDate()
    {
        return signDate;
    }

    public void setSignDate(String signDate)
    {
        this.signDate = signDate;
    }

    public List<String> getImages()
    {
        return images;
    }

    public void setImages(List<String> images)
    {
        this.images = images;
    }

    /**
     * @return sign_id
     */
    public String getSignId()
    {
        return signId;
    }

    /**
     * @param signId
     */
    public void setSignId(String signId)
    {
        this.signId = signId;
    }

    /**
     * 获取原应用中为org_id
     *
     * @return ecid - 原应用中为org_id
     */
    public String getEcid()
    {
        return ecid;
    }

    /**
     * 设置原应用中为org_id
     *
     * @param ecid 原应用中为org_id
     */
    public void setEcid(String ecid)
    {
        this.ecid = ecid;
    }

    /**
     * @return sign_time
     */
    public Date getSignTime()
    {
        return signTime;
    }

    /**
     * @param signTime
     */
    public void setSignTime(Date signTime)
    {
        this.setSignTimeStr(DateUtil.sdfHMS().format(signTime));
        this.signTime = signTime;
    }

    /**
     * @return longitude
     */
    public String getLongitude()
    {
        return longitude;
    }

    /**
     * @param longitude
     */
    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    /**
     * @return latitude
     */
    public String getLatitude()
    {
        return latitude;
    }

    /**
     * @param latitude
     */
    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
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
     * @return city
     */
    public String getCity()
    {
        return city;
    }

    /**
     * @param city
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    /**
     * @return content
     */
    public String getContent()
    {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content)
    {
        this.content = content;
    }

    /**
     * @return creator
     */
    public String getCreator()
    {
        return creator;
    }

    /**
     * @param creator
     */
    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    /**
     * @return creator_name
     */
    public String getCreatorName()
    {
        return creatorName;
    }

    /**
     * @param creatorName
     */
    public void setCreatorName(String creatorName)
    {
        this.creatorName = creatorName;
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
        this.setCreateTimeStr(DateUtil.sdfHMS().format(createTime));
        this.createTime = createTime;
    }

    /**
     * @return dep_id
     */
    public String getDepId()
    {
        return depId;
    }

    /**
     * @param depId
     */
    public void setDepId(String depId)
    {
        this.depId = depId;
    }

    /**
     * 获取暂时不用
     *
     * @return state - 暂时不用
     */
    public String getState()
    {
        return state;
    }

    /**
     * 设置暂时不用
     *
     * @param state 暂时不用
     */
    public void setState(String state)
    {
        this.state = state;
    }
}