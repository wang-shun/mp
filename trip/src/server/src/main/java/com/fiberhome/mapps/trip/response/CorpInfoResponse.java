package com.fiberhome.mapps.trip.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.CorplInfo;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class CorpInfoResponse extends BaseResponse {
	
	@XmlElement(name = "data")
	private CorplInfo data;

	public CorplInfo getData() {
		return data;
	}

	public void setData(CorplInfo data) {
		this.data = data;
	}

}
