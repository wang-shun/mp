package com.fiberhome.mapps.trip.entity;

import java.util.List;

public class HotelWithRmStatus {
	
	private List<Hotel> Hotels;// 酒店集合
	
	private List<RoomStatus> RoomStatus;// 房态集合
	
	private List<RoomType> RoomType;// 房型集合
	
	private Integer PageIndex;// 第几页
	
	private Integer PageSize;// 每页最大记录数
	
	private Integer RecordCount;// 总共记录数
	
	private Integer PageCount;// 最大页码

	public List<Hotel> getHotels() {
		return Hotels;
	}

	public void setHotels(List<Hotel> hotels) {
		Hotels = hotels;
	}

	public List<RoomStatus> getRoomStatus() {
		return RoomStatus;
	}

	public void setRoomStatus(List<RoomStatus> roomStatus) {
		RoomStatus = roomStatus;
	}

	public List<RoomType> getRoomType() {
		return RoomType;
	}

	public void setRoomType(List<RoomType> roomType) {
		RoomType = roomType;
	}

	public Integer getPageIndex() {
		return PageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		PageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return PageSize;
	}

	public void setPageSize(Integer pageSize) {
		PageSize = pageSize;
	}

	public Integer getRecordCount() {
		return RecordCount;
	}

	public void setRecordCount(Integer recordCount) {
		RecordCount = recordCount;
	}

	public Integer getPageCount() {
		return PageCount;
	}

	public void setPageCount(Integer pageCount) {
		PageCount = pageCount;
	}

}
