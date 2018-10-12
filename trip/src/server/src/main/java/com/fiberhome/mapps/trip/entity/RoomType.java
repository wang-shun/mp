package com.fiberhome.mapps.trip.entity;

import java.util.List;

public class RoomType {
	private Integer HotelGroup;// 集团编号（1：铂涛，2：华住，3：维也纳，4：东呈）
	private Integer HotelID;// 酒店编号
	private Integer RmTypeID;// 房型编号
	private String RmTypeName;// 房型编码
	private String ImgUrl;// 图片URL
	private List<ExtendInfo> Extends;// 扩展信息
	private String OtherInfo;// 其它信息

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

	public String getImgUrl() {
		return ImgUrl;
	}

	public void setImgUrl(String imgUrl) {
		ImgUrl = imgUrl;
	}

	public List<ExtendInfo> getExtends() {
		return Extends;
	}

	public void setExtends(List<ExtendInfo> extends1) {
		Extends = extends1;
	}

	public String getOtherInfo() {
		return OtherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		OtherInfo = otherInfo;
	}

}
