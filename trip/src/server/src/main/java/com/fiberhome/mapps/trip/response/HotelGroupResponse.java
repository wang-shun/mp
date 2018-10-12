package com.fiberhome.mapps.trip.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.CorplInfo;
import com.fiberhome.mapps.trip.entity.HotelGroup;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class HotelGroupResponse extends BaseResponse {
	
	@XmlElement(name = "data")
	private List<HotelGroup> data;

	public List<HotelGroup> getData() {
		return data;
	}

	public void setData(List<HotelGroup> data) {
		this.data = data;
	}

}
