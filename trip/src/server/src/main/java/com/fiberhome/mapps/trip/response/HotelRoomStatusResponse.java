package com.fiberhome.mapps.trip.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.RoomType;
import com.fiberhome.mapps.trip.entity.RoomStatus;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class HotelRoomStatusResponse  extends BaseResponse {
	
	@XmlElement
	private List<RoomStatus> data;

	public List<RoomStatus> getData() {
		return data;
	}

	public void setData(List<RoomStatus> data) {
		this.data = data;
	}

}
