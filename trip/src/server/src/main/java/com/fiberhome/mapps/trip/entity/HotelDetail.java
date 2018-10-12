package com.fiberhome.mapps.trip.entity;

import java.util.List;

public class HotelDetail {
	private Hotel Hotel;// 酒店信息
	private List Images;// 图片集合
	private List<RoomType> RoomType;// 房型集合
	private List<RoomStatus> RoomStatus;// 房态集合

	public Hotel getHotel() {
		return Hotel;
	}

	public void setHotel(Hotel hotel) {
		Hotel = hotel;
	}

	public List getImages() {
		return Images;
	}

	public void setImages(List images) {
		Images = images;
	}

	public List<RoomType> getRoomType() {
		return RoomType;
	}

	public void setRoomType(List<RoomType> roomType) {
		RoomType = roomType;
	}

	public List<RoomStatus> getRoomStatus() {
		return RoomStatus;
	}

	public void setRoomStatus(List<RoomStatus> roomStatus) {
		RoomStatus = roomStatus;
	}

}
