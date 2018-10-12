package com.fiberhome.mapps.activity.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.activity.entity.AtEnter;
import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryEnterResponse extends BaseResponse{
	@XmlElement(name = "enterList")
	private List<AtEnter> enterList;

	public List<AtEnter> getEnterList() {
		return enterList;
	}

	public void setEnterList(List<AtEnter> enterList) {
		this.enterList = enterList;
	}

}
