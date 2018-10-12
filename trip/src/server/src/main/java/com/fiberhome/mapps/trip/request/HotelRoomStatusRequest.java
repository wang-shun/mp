package com.fiberhome.mapps.trip.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.rop.AbstractRopRequest;

public class HotelRoomStatusRequest extends AbstractRopRequest {
	
	@NotNull
	private 	Integer	hotelID	;//	是	酒店编码
	
	private 	Integer	roomTypeID	;//		房型编码
	
	private 	Integer	rmTypeID	;

	@NotNull
	@Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$", message = "日期格式不合法,请使用【yyyy-MM-dd】")
	private 	String	beginDate	;//	是	入住日期

	@NotNull
	@Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$", message = "日期格式不合法,请使用【yyyy-MM-dd】")
	private 	String	endDate	;//	是	离店日期

	public Integer getRmTypeID()
	{
		return roomTypeID;
	}
	
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

	
}
