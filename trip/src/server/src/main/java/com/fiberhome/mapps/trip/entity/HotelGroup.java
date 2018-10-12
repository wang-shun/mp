package com.fiberhome.mapps.trip.entity;

import java.math.BigDecimal;
import java.util.Date;

public class HotelGroup {
	private 	Integer	groupid	;//		酒店集团信息表
	private 	String	fullname	;//		企业名称
	private 	String	shortname	;//		企业简称
	private 	String	licence	;//		企业执照ID
	private 	String	legalperson	;//		企业法人
	private 	String	contact	;//		企业联系人
	private 	String	tel	;//		联系电话
	private 	Integer	zipcode	;//		邮编
	private 	Integer	province	;//		省份
	private 	Integer	city	;//		城市
	private 	Integer	district	;//		行政区域
	private 	String	street	;//		街道
	private 	String	fulladdrss	;//		省份+城市+地区+街道   
	private 	Integer	hasadvancepay	;//		预付 2不可以 1可以
	private 	Integer	hasspotpay	;//		现付 2不可以 1可以
	private 	Integer	hascreditpay	;//		后付 2不可以 1可以
	private 	BigDecimal	advancepaydiscount	;//		预付折扣 7.5折
	private 	BigDecimal	spotpaydiscount	;//		现付折扣 8.5折
	public Integer getGroupid() {
		return groupid;
	}
	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public String getLicence() {
		return licence;
	}
	public void setLicence(String licence) {
		this.licence = licence;
	}
	public String getLegalperson() {
		return legalperson;
	}
	public void setLegalperson(String legalperson) {
		this.legalperson = legalperson;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Integer getZipcode() {
		return zipcode;
	}
	public void setZipcode(Integer zipcode) {
		this.zipcode = zipcode;
	}
	public Integer getProvince() {
		return province;
	}
	public void setProvince(Integer province) {
		this.province = province;
	}
	public Integer getCity() {
		return city;
	}
	public void setCity(Integer city) {
		this.city = city;
	}
	public Integer getDistrict() {
		return district;
	}
	public void setDistrict(Integer district) {
		this.district = district;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getFulladdrss() {
		return fulladdrss;
	}
	public void setFulladdrss(String fulladdrss) {
		this.fulladdrss = fulladdrss;
	}
	public Integer getHasadvancepay() {
		return hasadvancepay;
	}
	public void setHasadvancepay(Integer hasadvancepay) {
		this.hasadvancepay = hasadvancepay;
	}
	public Integer getHasspotpay() {
		return hasspotpay;
	}
	public void setHasspotpay(Integer hasspotpay) {
		this.hasspotpay = hasspotpay;
	}
	public Integer getHascreditpay() {
		return hascreditpay;
	}
	public void setHascreditpay(Integer hascreditpay) {
		this.hascreditpay = hascreditpay;
	}
	public BigDecimal getAdvancepaydiscount() {
		return advancepaydiscount;
	}
	public void setAdvancepaydiscount(BigDecimal advancepaydiscount) {
		this.advancepaydiscount = advancepaydiscount;
	}
	public BigDecimal getSpotpaydiscount() {
		return spotpaydiscount;
	}
	public void setSpotpaydiscount(BigDecimal spotpaydiscount) {
		this.spotpaydiscount = spotpaydiscount;
	}
	public BigDecimal getCreditpaydiscount() {
		return creditpaydiscount;
	}
	public void setCreditpaydiscount(BigDecimal creditpaydiscount) {
		this.creditpaydiscount = creditpaydiscount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getSigningtime() {
		return signingtime;
	}
	public void setSigningtime(Date signingtime) {
		this.signingtime = signingtime;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	private 	BigDecimal	creditpaydiscount	;//		后付折扣 9折
	private 	String	remark	;//		备注
	private 	Date	signingtime	;//		签约时间
	private 	Date	createtime	;//		创建时间
	private 	Integer	state	;//		态状 1正常2已下架
	private 	String	website	;//		集团官网
	private 	String	description	;//		介绍

}
