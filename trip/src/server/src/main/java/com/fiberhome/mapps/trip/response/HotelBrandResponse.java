package com.fiberhome.mapps.trip.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.HotelBrand;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class HotelBrandResponse extends BaseResponse {
	
	@XmlElement
	private List<HotelBrand> data;

	public List<HotelBrand> getData() {
		return data;
	}

	public void setData(List<HotelBrand> data) {
		this.data = data;
	}

}
