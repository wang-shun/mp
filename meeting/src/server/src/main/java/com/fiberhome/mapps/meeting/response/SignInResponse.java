package com.fiberhome.mapps.meeting.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.meeting.entity.ClientMeetingInfo;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class SignInResponse extends BaseResponse{
	@XmlElement(name = "signNum")
	private long     signNum;
	
	@XmlElement(name = "personName")
    private String   personName;

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public long getSignNum() {
		return signNum;
	}

	public void setSignNum(long signNum) {
		this.signNum = signNum;
	}
	
	
}
