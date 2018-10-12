package com.fiberhome.mapps.trip.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.HotelDetail;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class HotelDetailResponse extends BaseResponse {
	@XmlElement
	private HotelDetail data;

	public HotelDetail getData() {
		return data;
	}

	public void setData(HotelDetail data) {
		this.data = data;
	}

}
