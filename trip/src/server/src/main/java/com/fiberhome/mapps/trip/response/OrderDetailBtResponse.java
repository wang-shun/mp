package com.fiberhome.mapps.trip.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.HotelOrderDetail;
import com.fiberhome.mapps.trip.entity.TripOrder;
import com.fiberhome.mapps.trip.entity.TripOrderPage;
import com.fiberhome.mapps.trip.entity.TripOrderPageBt;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class OrderDetailBtResponse extends BaseResponse {
	
	@XmlElement
	private HotelOrderDetail data;

	public HotelOrderDetail getData()
	{
		return data;
	}

	public void setData(HotelOrderDetail data)
	{
		this.data = data;
	}


}
