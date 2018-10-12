package com.fiberhome.mapps.activity.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "AT_ENTER")
public class AtEnter {
    @Id
    @Column(name = "enter_id")
    private String enterId;

    @Column(name = "act_id")
    private String actId;	

    private String phone;

    private String name;
    
    private String deptName;

    @Column(name = "id_card")
    private String idCard;

    private String remark;

    private String sex;

    @Column(name = "enter_time")
    private Date enterTime;

    private String ecid;
    
    private String enterPersonId;

    /**
     * @return enter_id
     */
    public String getEnterId() {
        return enterId;
    }

    /**
     * @param enterId
     */
    public void setEnterId(String enterId) {
        this.enterId = enterId;
    }

    /**
     * @return act_id
     */
    public String getActId() {
        return actId;
    }

    /**
     * @param actId
     */
    public void setActId(String actId) {
        this.actId = actId;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return id_card
     */
    public String getIdCard() {
        return idCard;
    }

    /**
     * @param idCard
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return enter_time
     */
    public Date getEnterTime() {
        return enterTime;
    }

    /**
     * @param enterTime
     */
    public void setEnterTime(Date enterTime) {
        this.enterTime = enterTime;
    }

    /**
     * @return ecid
     */
    public String getEcid() {
        return ecid;
    }

    /**
     * @param ecid
     */
    public void setEcid(String ecid) {
        this.ecid = ecid;
    }

	public String getEnterPersonId() {
		return enterPersonId;
	}

	public void setEnterPersonId(String enterPersonId) {
		this.enterPersonId = enterPersonId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}