package com.fiberhome.mapps.trip.entity;

import java.math.BigDecimal;
import java.util.Date;

public class CorplInfo {
	private String fulladdrss;// 公司地址
	private Integer corpid;// 公司编号
	private String city;// 地址编号
	private Integer hasspotpay;// 是否支持现付0：不支持，1支持
	private String salesname;// 销售员姓名
	private String creditpaydiscount;// 管理员ID
	private BigDecimal settingapplystrict;// 付后折扣
	private String guidkey;// 公司唯一标识
	private String remark;// 备注
	private BigDecimal spotpaydiscount;// 现付折扣
	private Integer travelapplyflowtype;// 出差审批流程方案（1部门领导审批，2直属领导审批，3助理审批，99无）
	private Integer applysource;// 申请来源 1官网 2微信
	private BigDecimal balance;// 储值余额
	private String province;// 城市编码
	private Integer countstaff;// 员工数
	public String getFulladdrss() {
		return fulladdrss;
	}
	public void setFulladdrss(String fulladdrss) {
		this.fulladdrss = fulladdrss;
	}
	public Integer getCorpid() {
		return corpid;
	}
	public void setCorpid(Integer corpid) {
		this.corpid = corpid;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getHasspotpay() {
		return hasspotpay;
	}
	public void setHasspotpay(Integer hasspotpay) {
		this.hasspotpay = hasspotpay;
	}
	public String getSalesname() {
		return salesname;
	}
	public void setSalesname(String salesname) {
		this.salesname = salesname;
	}
	public String getCreditpaydiscount() {
		return creditpaydiscount;
	}
	public void setCreditpaydiscount(String creditpaydiscount) {
		this.creditpaydiscount = creditpaydiscount;
	}
	public BigDecimal getSettingapplystrict() {
		return settingapplystrict;
	}
	public void setSettingapplystrict(BigDecimal settingapplystrict) {
		this.settingapplystrict = settingapplystrict;
	}
	public String getGuidkey() {
		return guidkey;
	}
	public void setGuidkey(String guidkey) {
		this.guidkey = guidkey;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public BigDecimal getSpotpaydiscount() {
		return spotpaydiscount;
	}
	public void setSpotpaydiscount(BigDecimal spotpaydiscount) {
		this.spotpaydiscount = spotpaydiscount;
	}
	public Integer getTravelapplyflowtype() {
		return travelapplyflowtype;
	}
	public void setTravelapplyflowtype(Integer travelapplyflowtype) {
		this.travelapplyflowtype = travelapplyflowtype;
	}
	public Integer getApplysource() {
		return applysource;
	}
	public void setApplysource(Integer applysource) {
		this.applysource = applysource;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public Integer getCountstaff() {
		return countstaff;
	}
	public void setCountstaff(Integer countstaff) {
		this.countstaff = countstaff;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getDockingoa() {
		return dockingoa;
	}
	public void setDockingoa(String dockingoa) {
		this.dockingoa = dockingoa;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public BigDecimal getTotalconsume() {
		return totalconsume;
	}
	public void setTotalconsume(BigDecimal totalconsume) {
		this.totalconsume = totalconsume;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public Integer getHascreditpay() {
		return hascreditpay;
	}
	public void setHascreditpay(Integer hascreditpay) {
		this.hascreditpay = hascreditpay;
	}
	public Integer getApplyid() {
		return applyid;
	}
	public void setApplyid(Integer applyid) {
		this.applyid = applyid;
	}
	public Integer getSalesid() {
		return salesid;
	}
	public void setSalesid(Integer salesid) {
		this.salesid = salesid;
	}
	public Integer getHasadvancepay() {
		return hasadvancepay;
	}
	public void setHasadvancepay(Integer hasadvancepay) {
		this.hasadvancepay = hasadvancepay;
	}
	public BigDecimal getAdvancepaydiscount() {
		return advancepaydiscount;
	}
	public void setAdvancepaydiscount(BigDecimal advancepaydiscount) {
		this.advancepaydiscount = advancepaydiscount;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	private String contact;// 企业联系人
	private String dockingoa;//
	private String tel;// 联系电话
	private int state;// 状态（1未审核，2正常合作，3暂停合作，99终止合作，100作废）
	private Date createtime;// 创建时间（时间戳）
	private BigDecimal totalconsume;// 总消费
	private String shortname;// 简称
	private Integer hascreditpay;// 否是支付后付0：不支持，1支持
	private Integer applyid;// 申请编号
	private Integer salesid;// 销售员编号
	private Integer hasadvancepay;// 是否支付预付0：不支持，1支持
	private BigDecimal advancepaydiscount;// 预付折扣
	private String district;// 地区
	private String fullname;// 公司全称

}
