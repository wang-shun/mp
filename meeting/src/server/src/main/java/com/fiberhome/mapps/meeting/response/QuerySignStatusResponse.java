package com.fiberhome.mapps.meeting.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QuerySignStatusResponse extends BaseResponse
{
    @XmlElement(name = "totalnum")
    private int totalnum;
    
    @XmlElement(name = "signnum")
    private int signnum;

    public int getTotalnum() {
		return totalnum;
	}

	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}

	public int getSignnum() {
		return signnum;
	}

	public void setSignnum(int signnum) {
		this.signnum = signnum;
	}
}
