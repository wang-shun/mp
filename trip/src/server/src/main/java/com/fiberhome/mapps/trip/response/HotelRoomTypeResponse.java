package com.fiberhome.mapps.trip.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.RoomType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class HotelRoomTypeResponse  extends BaseResponse {
	
	@XmlElement
	private List<RoomType> data;

	public List<RoomType> getData() {
		return data;
	}

	public void setData(List<RoomType> data) {
		this.data = data;
	}



}
