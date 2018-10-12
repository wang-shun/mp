package com.fiberhome.mapps.activity.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class EnterRequest extends AbstractRopRequest{
	
	/**
	 * 活动Id
	 */
	@NotNull
	private String actId;
	/**
	 * 手机号码
	 */
	@NotNull
	private String phone;
	/**
	 * 姓名
	 */
	@NotNull
	private String name;
	/**
	 * 身份证
	 */
	private String idCard;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 性别
	 */
	private String sex;
	public String getActId() {
		return actId;
	}
	public void setActId(String actId) {
		this.actId = actId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
}
