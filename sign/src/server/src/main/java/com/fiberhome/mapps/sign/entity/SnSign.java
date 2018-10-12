package com.fiberhome.mapps.sign.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fiberhome.mapps.intergration.mybatis.OrgDept;

@OrgDept(orgDepId = "orgDeptId", orgDeptOrder = "orgDeptOrder")
@Table(name = "SN_SIGN")
public class SnSign
{
    @Id
    @Column(name = "sign_id")
    private String signId;

    /**
     * 原应用中为org_id
     */
    private String ecid;

    @Column(name = "sign_time")
    private Date   signTime;

    private String longitude;

    private String latitude;

    private String address;

    private String city;

    private String content;

    private String creator;

    @Column(name = "creator_name")
    private String creatorName;

    @Column(name = "create_time")
    private Date   createTime;

    @Column(name = "dep_id")
    private String depId;

    /**
     * 暂时不用
     */
    private String state;

    @Column(name = "task_no")
    private String taskNo;

    @Column(name = "dep_order")
    private String deptOrder;
    
    @Column(name = "org_dept_id")
    private String       orgDeptId;     //所属部门机构id
    @Column(name = "org_dept_order")
    private String       orgDeptOrder; //所属部门机构order
    
    public String getOrgDeptId() {
		return orgDeptId;
	}

	public void setOrgDeptId(String orgDeptId) {
		this.orgDeptId = orgDeptId;
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

    public String getTaskNo()
    {
        return taskNo;
    }

    public void setTaskNo(String taskNo)
    {
        this.taskNo = taskNo;
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