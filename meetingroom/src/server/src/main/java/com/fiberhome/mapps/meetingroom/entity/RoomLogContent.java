package com.fiberhome.mapps.meetingroom.entity;

public class RoomLogContent extends BaseOpLogContent{
	private String roomId;
	
    private String roomName;
    
    public RoomLogContent(String roomId ,String roomName, String message){
    	this.setMessage(message);
    	this.roomId = roomId;
    	this.roomName = roomName;
    }
    
    public String toString(){
    	return "roomId=" +roomId+ ",roomName="+roomName+",message="+this.getMessage();
    }

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
    
}
