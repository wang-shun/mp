package com.fiberhome.mapps.trip.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.HotelWithRmStatus;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class HotelWithRmStatusResponse  extends BaseResponse {
	
	@XmlElement
	private HotelWithRmStatus data;

	public HotelWithRmStatus getData() {
		return data;
	}

	public void setData(HotelWithRmStatus data) {
		this.data = data;
	}

}
