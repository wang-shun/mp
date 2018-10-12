package com.fiberhome.mapps.trip.entity;


import java.util.List;

public class RoomStatusWithType {
	
	private 	List<RoomType>	RoomType	;//		房型集合
	private 	List<RoomStatus> 	RoomStatus	;//		房态集合
	
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
