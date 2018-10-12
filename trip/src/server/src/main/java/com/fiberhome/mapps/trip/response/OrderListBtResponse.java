package com.fiberhome.mapps.trip.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.TripOrder;
import com.fiberhome.mapps.trip.entity.TripOrderPage;
import com.fiberhome.mapps.trip.entity.TripOrderPageBt;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class OrderListBtResponse extends BaseResponse {
	
	@XmlElement
	private TripOrderPageBt data;

	public TripOrderPageBt getData()
	{
		return data;
	}

	public void setData(TripOrderPageBt data)
	{
		this.data = data;
	}


}
