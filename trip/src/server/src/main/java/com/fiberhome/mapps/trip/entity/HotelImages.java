package com.fiberhome.mapps.trip.entity;

public class HotelImages {
	
	private Integer HotelGroup;// 集团编号（1：铂涛，2：华住，3：维也纳，4：东呈）
	private Integer HotelID;// 酒店编号
	private String ImgUrl;// 图片URL
	private Integer ImgType;// 图片类型（1：酒店，2：客房）
	private Integer RmTypeID;// 房型编号

	public Integer getHotelGroup() {
		return HotelGroup;
	}

	public void setHotelGroup(Integer hotelGroup) {
		HotelGroup = hotelGroup;
	}

	public Integer getHotelID() {
		return HotelID;
	}

	public void setHotelID(Integer hotelID) {
		HotelID = hotelID;
	}

	public String getImgUrl() {
		return ImgUrl;
	}

	public void setImgUrl(String imgUrl) {
		ImgUrl = imgUrl;
	}

	public Integer getImgType() {
		return ImgType;
	}

	public void setImgType(Integer imgType) {
		ImgType = imgType;
	}

	public Integer getRmTypeID() {
		return RmTypeID;
	}

	public void setRmTypeID(Integer rmTypeID) {
		RmTypeID = rmTypeID;
	}

}
