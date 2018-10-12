package com.fiberhome.mapps.trip.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class TripUserResponse extends BaseResponse
{
    @XmlElement(name = "data")
    private MyUser data;

	public MyUser getData()
	{
		return data;
	}

	public void setData(MyUser data)
	{
		this.data = data;
	}

}
