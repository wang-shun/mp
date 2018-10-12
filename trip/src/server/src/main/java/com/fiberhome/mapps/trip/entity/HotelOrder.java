package com.fiberhome.mapps.trip.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HotelOrder
{
	
	private	String	orderID	;//		
	private	String	orderCode	;//		订单编号
	private	String	outerOrderCode	;//		下游第三方订单编号
	private	Integer	orderStatus	;//		订单状态（1：待付款，2：本地取消，3：待酒店确认，4：预定成功，5：预定失败，100：酒店已取消，101：noshow，102：在住，103：离店，104：已评论）
	private	Integer	orderType	;//		订单类型（1：公差，2：私差）
	private	Integer	corpID	;//		公司编号
	private	String	corpName	;//		公司名称
	private	Integer	brandID	;//		店酒品牌ID
	private	String	brandName	;//		店酒品牌
	private	Integer	staffID	;//		员工ID
	private	String	staffMobile	;//		员工手机
	private	Integer	travelApplyID	;//		差旅申请ID（下订时关联差旅申请记录）
	private	Integer	hotelGroupID	;//		酒店集团ID
	private	String	hotelGroupName	;//		酒店集团
	private	Integer	hotelID	;//		酒店ID
	private	String	hotelName	;//		酒店名称
	private	String	hotelTel	;//		酒店电话
	private	String	hotelAddr	;//		店酒地址
	private	String	cityCode	;//		城市编码
	private	String	cityName	;//		城市名称
	private	Integer	roomTypeID	;//		型房ID
	private	String	roomTypeName	;//		房型名
	private	String	contactName	;//		联系人
	private	String	contactPhone	;//		联系电话
	private	String	guestsName	;//		入住人（多个以,逗号分隔）
	private	Integer	roomNight	;//		房晚
	private	Integer	roomCount	;//		房间数
	private	BigDecimal	firstNightRate	;//		首晚房价
	private	Date	checkInTime	;//		订单入住时间
	private	Date	checkOutTime	;//		订单离店时间
	private	Date	arivalTime	;//		抵店时间
	private	Date	departTime	;//		离店时间
	private	Integer	paymentType	;//		支付方式：1.储值支付 2到付 3支付宝 4微信
	private	String	paySerialNo	;//		支付流水号
	private	Date	payTime	;//		支付时间
	private	BigDecimal	totalAmount	;//		单订总金额
	private	Integer	hasBreakfast	;//		是否有早餐
	private	String	marketCode	;//		市场活动编码
	private	Date	updateTime	;//		更新时间
	private	Date	createTime	;//		创建时间
	private	String	remark	;//		备注
	
	
	
	private	String	checkInTimeString	;//		订单入住时间
	private	String	checkOutTimeString	;//		订单离店时间
	
	public String getCheckInTimeString()
	{
		return checkInTimeString;
	}
	public void setCheckInTimeString(String checkInTimeString)
	{
		this.checkInTimeString = checkInTimeString;
	}
	public String getCheckOutTimeString()
	{
		return checkOutTimeString;
	}
	public void setCheckOutTimeString(String checkOutTimeString)
	{
		this.checkOutTimeString = checkOutTimeString;
	}
	public String getOrderID()
	{
		return orderID;
	}
	public void setOrderID(String orderID)
	{
		this.orderID = orderID;
	}
	public String getOrderCode()
	{
		return orderCode;
	}
	public void setOrderCode(String orderCode)
	{
		this.orderCode = orderCode;
	}
	public String getOuterOrderCode()
	{
		return outerOrderCode;
	}
	public void setOuterOrderCode(String outerOrderCode)
	{
		this.outerOrderCode = outerOrderCode;
	}
	public Integer getOrderStatus()
	{
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus)
	{
		this.orderStatus = orderStatus;
	}
	public Integer getOrderType()
	{
		return orderType;
	}
	public void setOrderType(Integer orderType)
	{
		this.orderType = orderType;
	}
	public Integer getCorpID()
	{
		return corpID;
	}
	public void setCorpID(Integer corpID)
	{
		this.corpID = corpID;
	}
	public String getCorpName()
	{
		return corpName;
	}
	public void setCorpName(String corpName)
	{
		this.corpName = corpName;
	}
	public Integer getBrandID()
	{
		return brandID;
	}
	public void setBrandID(Integer brandID)
	{
		this.brandID = brandID;
	}
	public String getBrandName()
	{
		return brandName;
	}
	public void setBrandName(String brandName)
	{
		this.brandName = brandName;
	}
	public Integer getStaffID()
	{
		return staffID;
	}
	public void setStaffID(Integer staffID)
	{
		this.staffID = staffID;
	}
	public String getStaffMobile()
	{
		return staffMobile;
	}
	public void setStaffMobile(String staffMobile)
	{
		this.staffMobile = staffMobile;
	}
	public Integer getTravelApplyID()
	{
		return travelApplyID;
	}
	public void setTravelApplyID(Integer travelApplyID)
	{
		this.travelApplyID = travelApplyID;
	}
	public Integer getHotelGroupID()
	{
		return hotelGroupID;
	}
	public void setHotelGroupID(Integer hotelGroupID)
	{
		this.hotelGroupID = hotelGroupID;
	}
	public String getHotelGroupName()
	{
		return hotelGroupName;
	}
	public void setHotelGroupName(String hotelGroupName)
	{
		this.hotelGroupName = hotelGroupName;
	}
	public Integer getHotelID()
	{
		return hotelID;
	}
	public void setHotelID(Integer hotelID)
	{
		this.hotelID = hotelID;
	}
	public String getHotelName()
	{
		return hotelName;
	}
	public void setHotelName(String hotelName)
	{
		this.hotelName = hotelName;
	}
	public String getHotelTel()
	{
		return hotelTel;
	}
	public void setHotelTel(String hotelTel)
	{
		this.hotelTel = hotelTel;
	}
	public String getHotelAddr()
	{
		return hotelAddr;
	}
	public void setHotelAddr(String hotelAddr)
	{
		this.hotelAddr = hotelAddr;
	}
	public String getCityCode()
	{
		return cityCode;
	}
	public void setCityCode(String cityCode)
	{
		this.cityCode = cityCode;
	}
	public String getCityName()
	{
		return cityName;
	}
	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}
	public Integer getRoomTypeID()
	{
		return roomTypeID;
	}
	public void setRoomTypeID(Integer roomTypeID)
	{
		this.roomTypeID = roomTypeID;
	}
	public String getRoomTypeName()
	{
		return roomTypeName;
	}
	public void setRoomTypeName(String roomTypeName)
	{
		this.roomTypeName = roomTypeName;
	}
	public String getContactName()
	{
		return contactName;
	}
	public void setContactName(String contactName)
	{
		this.contactName = contactName;
	}
	public String getContactPhone()
	{
		return contactPhone;
	}
	public void setContactPhone(String contactPhone)
	{
		this.contactPhone = contactPhone;
	}
	public String getGuestsName()
	{
		return guestsName;
	}
	public void setGuestsName(String guestsName)
	{
		this.guestsName = guestsName;
	}
	public Integer getRoomNight()
	{
		return roomNight;
	}
	public void setRoomNight(Integer roomNight)
	{
		this.roomNight = roomNight;
	}
	public Integer getRoomCount()
	{
		return roomCount;
	}
	public void setRoomCount(Integer roomCount)
	{
		this.roomCount = roomCount;
	}
	public BigDecimal getFirstNightRate()
	{
		return firstNightRate;
	}
	public void setFirstNightRate(BigDecimal firstNightRate)
	{
		this.firstNightRate = firstNightRate;
	}
	public Date getCheckInTime()
	{
		return checkInTime;
	}
	public void setCheckInTime(Date checkInTime)
	{
		this.checkInTime = checkInTime;
		if(checkInTime != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			this.setCheckInTimeString(sdf.format(checkInTime));
		}
	}
	public Date getCheckOutTime()
	{
		return checkOutTime;
	}
	public void setCheckOutTime(Date checkOutTime)
	{
		this.checkOutTime = checkOutTime;
		if(checkOutTime != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			this.setCheckOutTimeString(sdf.format(checkOutTime));
		}
	}
	public Date getArivalTime()
	{
		return arivalTime;
	}
	public void setArivalTime(Date arivalTime)
	{
		this.arivalTime = arivalTime;
	}
	public Date getDepartTime()
	{
		return departTime;
	}
	public void setDepartTime(Date departTime)
	{
		this.departTime = departTime;
	}
	public Integer getPaymentType()
	{
		return paymentType;
	}
	public void setPaymentType(Integer paymentType)
	{
		this.paymentType = paymentType;
	}
	public String getPaySerialNo()
	{
		return paySerialNo;
	}
	public void setPaySerialNo(String paySerialNo)
	{
		this.paySerialNo = paySerialNo;
	}
	public Date getPayTime()
	{
		return payTime;
	}
	public void setPayTime(Date payTime)
	{
		this.payTime = payTime;
	}
	public BigDecimal getTotalAmount()
	{
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount)
	{
		this.totalAmount = totalAmount;
	}
	public Integer getHasBreakfast()
	{
		return hasBreakfast;
	}
	public void setHasBreakfast(Integer hasBreakfast)
	{
		this.hasBreakfast = hasBreakfast;
	}
	public String getMarketCode()
	{
		return marketCode;
	}
	public void setMarketCode(String marketCode)
	{
		this.marketCode = marketCode;
	}
	public Date getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}

}
