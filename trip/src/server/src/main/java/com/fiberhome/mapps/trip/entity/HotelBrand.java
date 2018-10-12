package com.fiberhome.mapps.trip.entity;

import java.util.Date;

public class HotelBrand {
	private 	Integer	brandID	;//		酒店品牌ID
	private 	String	outerBrandCode	;//		第三方品牌编号
	private 	Integer	hotelGroupID	;//		集团ID
	private 	String	brandName	;//		品牌名称  7天  孋枫
	private 	Integer	maxRoomStatusDays	;//		最大可预定天数
	private 	String	description	;//		介绍
	private 	String	remark	;//		备注
	private 	Date	createTime	;//		创建时间
	private 	Date	lastGatherTime	;//		最后采集时间
	private 	Integer	state	;//		状态  1.正常 2.已下架
	private 	Integer	maxStayDays	;//		最大可预定天数
	
	public Integer getBrandID() {
		return brandID;
	}
	public void setBrandID(Integer brandID) {
		this.brandID = brandID;
	}
	public String getOuterBrandCode() {
		return outerBrandCode;
	}
	public void setOuterBrandCode(String outerBrandCode) {
		this.outerBrandCode = outerBrandCode;
	}
	public Integer getHotelGroupID() {
		return hotelGroupID;
	}
	public void setHotelGroupID(Integer hotelGroupID) {
		this.hotelGroupID = hotelGroupID;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public Integer getMaxRoomStatusDays() {
		return maxRoomStatusDays;
	}
	public void setMaxRoomStatusDays(Integer maxRoomStatusDays) {
		this.maxRoomStatusDays = maxRoomStatusDays;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastGatherTime() {
		return lastGatherTime;
	}
	public void setLastGatherTime(Date lastGatherTime) {
		this.lastGatherTime = lastGatherTime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getMaxStayDays() {
		return maxStayDays;
	}
	public void setMaxStayDays(Integer maxStayDays) {
		this.maxStayDays = maxStayDays;
	}

}
