package com.fiberhome.mapps.trip.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.TripOrder;
import com.fiberhome.mapps.trip.entity.TripOrderPage;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class OrderListResponse extends BaseResponse {
	
	@XmlElement
	private TripOrderPage data;

	public TripOrderPage getData() {
		return data;
	}

	public void setData(TripOrderPage data) {
		this.data = data;
	}

}