package com.fiberhome.mapps.activity.response;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class EnterResponse extends BaseResponse{
	/**
	 * 报名成功与否 
	 */
	@NotNull
	@XmlElement
	private String message;
	/**
	 * 活动报名人数
	 */
	@XmlElement
	private String enterNum;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getEnterNum() {
		return enterNum;
	}
	public void setEnterNum(String enterNum) {
		this.enterNum = enterNum;
	}
}
