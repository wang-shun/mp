package com.fiberhome.mapps.trip.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.HotelImages;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class HotelImagesResponse  extends BaseResponse {
	
	@XmlElement
	private List<HotelImages> data;

	public List<HotelImages> getData() {
		return data;
	}

	public void setData(List<HotelImages> data) {
		this.data = data;
	}



}
