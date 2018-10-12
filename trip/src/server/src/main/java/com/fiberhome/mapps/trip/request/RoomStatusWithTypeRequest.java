package com.fiberhome.mapps.trip.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.rop.AbstractRopRequest;

public class RoomStatusWithTypeRequest extends AbstractRopRequest {

	private Integer[] hotelGroup;// 酒店集团编号数组

	private Integer[] hotelIds;// 酒店编号数组

	private Integer[] roomTypeID;// 房态编号

	@NotNull
	@Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$", message = "日期格式不合法,请使用【yyyy-MM-dd】")
	private String beginDate;// 入住时间时间戳

	@NotNull
	@Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$", message = "日期格式不合法,请使用【yyyy-MM-dd】")
	private String endDate;// 离店时间时间戳

	public Integer[] getHotelGroup() {
		return hotelGroup;
	}

	public void setHotelGroup(Integer[] hotelGroup) {
		this.hotelGroup = hotelGroup;
	}

	public Integer[] getHotelIds() {
		return hotelIds;
	}

	public void setHotelIds(Integer[] hotelIds) {
		this.hotelIds = hotelIds;
	}

	public Integer[] getRoomTypeID() {
		return roomTypeID;
	}

	public void setRoomTypeID(Integer[] roomTypeID) {
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
