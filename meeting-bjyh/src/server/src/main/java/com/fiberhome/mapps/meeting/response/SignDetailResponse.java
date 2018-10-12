package com.fiberhome.mapps.meeting.response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.meeting.entity.SignDetail;

public class SignDetailResponse extends BaseResponse{
	
	@XmlElement(name = "signDetaiList")
	List<SignDetail> signDetaiList;

	@XmlElement(name = "total")
    private long                    total;
	
	public List<SignDetail> getSignDetaiList() {
		return signDetaiList;
	}

	public void setSignDetaiList(List<SignDetail> signDetaiList) {
		this.signDetaiList = signDetaiList;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
	
	
}	
