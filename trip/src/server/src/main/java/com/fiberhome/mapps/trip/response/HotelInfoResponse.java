package com.fiberhome.mapps.trip.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.HotelInfo;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class HotelInfoResponse extends BaseResponse {
	
	@XmlElement
	private HotelInfo data;

	public HotelInfo getData() {
		return data;
	}

	public void setData(HotelInfo data) {
		this.data = data;
	}


}
