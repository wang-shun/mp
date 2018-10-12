package com.fiberhome.mapps.trip.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.RoomStatusWithType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class RoomStatusWithTypeResponse extends BaseResponse {
	@XmlElement
	private RoomStatusWithType data;

	public RoomStatusWithType getData() {
		return data;
	}

	public void setData(RoomStatusWithType data) {
		this.data = data;
	}

}
