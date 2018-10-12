package com.fiberhome.mapps.trip.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.validation.constraints.Pattern;

import com.rop.AbstractRopRequest;

public class HotelDetailRequest extends AbstractRopRequest {
	private Integer hotelID;// 酒店编号
	private Integer roomTypeID;// 房态编号
	
	@Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$", message = "日期格式不合法,请使用【yyyy-MM-dd】")
	private String beginDate;// 入住日期时间戳
	@Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$", message = "日期格式不合法,请使用【yyyy-MM-dd】")
	private String endDate;// 离店日期时间戳
	private Boolean includeImg = true;// 是否包含图片
	private Boolean returnAllRmStatus = true ;// 是否包含所有房态

	public Integer getHotelID() {
		return hotelID;
	}

	public void setHotelID(Integer hotelID) {
		this.hotelID = hotelID;
	}

	public Integer getRoomTypeID() {
		return roomTypeID;
	}

	public void setRoomTypeID(Integer roomTypeID) {
		this.roomTypeID = roomTypeID;
	}


	public long getBeginDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(beginDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0l ;
		}
	}
	

	public Long getEndDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(endDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0l ;
		}
	}
	
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Boolean getIncludeImg() {
		return includeImg;
	}

	public void setIncludeImg(Boolean includeImg) {
		this.includeImg = includeImg;
	}

	public Boolean getReturnAllRmStatus() {
		return returnAllRmStatus;
	}

	public void setReturnAllRmStatus(Boolean returnAllRmStatus) {
		this.returnAllRmStatus = returnAllRmStatus;
	}

}
