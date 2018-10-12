package com.fiberhome.mapps.trip.entity;

import java.math.BigDecimal;
import java.util.Date;

public class RoomStatus {

	private Integer RmTypeID;// 房型ID
	private String RmTypeName;// 房型名
	private BigDecimal Price;// 原价
	private BigDecimal RackPrice;// 门市价
	private BigDecimal AdvancePrice;// 预付价
	private BigDecimal SpotPrice;// 现付价
	private Date EndOfDay;// 营业日（yyyy-MM-dd）
	private Integer HotelID;// 酒店ID
	private Boolean Bookable;// 是否可预订 ([Bookable][Saleable]两个参数参数同时成立才可预订)
	private Integer Saleable;// 可售数 ([Bookable][Saleable]两个参数参数同时成立才可预订)

	public Integer getRmTypeID() {
		return RmTypeID;
	}

	public void setRmTypeID(Integer rmTypeID) {
		RmTypeID = rmTypeID;
	}

	public String getRmTypeName() {
		return RmTypeName;
	}

	public void setRmTypeName(String rmTypeName) {
		RmTypeName = rmTypeName;
	}

	public BigDecimal getPrice() {
		return Price;
	}

	public void setPrice(BigDecimal price) {
		Price = price;
	}

	public BigDecimal getRackPrice() {
		return RackPrice;
	}

	public void setRackPrice(BigDecimal rackPrice) {
		RackPrice = rackPrice;
	}

	public BigDecimal getAdvancePrice() {
		return AdvancePrice;
	}

	public void setAdvancePrice(BigDecimal advancePrice) {
		AdvancePrice = advancePrice;
	}

	public BigDecimal getSpotPrice() {
		return SpotPrice;
	}

	public void setSpotPrice(BigDecimal spotPrice) {
		SpotPrice = spotPrice;
	}

	public Date getEndOfDay() {
		return EndOfDay;
	}

	public void setEndOfDay(Date endOfDay) {
		EndOfDay = endOfDay;
	}

	public Integer getHotelID() {
		return HotelID;
	}

	public void setHotelID(Integer hotelID) {
		HotelID = hotelID;
	}

	public Boolean getBookable() {
		return Bookable;
	}

	public void setBookable(Boolean bookable) {
		Bookable = bookable;
	}

	public Integer getSaleable() {
		return Saleable;
	}

	public void setSaleable(Integer saleable) {
		Saleable = saleable;
	}

}
