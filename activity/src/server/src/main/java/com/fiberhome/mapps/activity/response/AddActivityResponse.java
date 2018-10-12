package com.fiberhome.mapps.activity.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class AddActivityResponse extends BaseResponse
{
    @XmlElement
    private String actId;

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}


}
