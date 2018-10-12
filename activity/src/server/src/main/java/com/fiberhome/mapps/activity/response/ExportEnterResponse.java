package com.fiberhome.mapps.activity.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class ExportEnterResponse extends BaseResponse{
	@XmlElement
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
