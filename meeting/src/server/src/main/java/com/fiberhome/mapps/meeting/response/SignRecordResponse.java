package com.fiberhome.mapps.meeting.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.meeting.entity.SignDetail;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class SignRecordResponse extends BaseResponse{
	
	@XmlElement(name = "signRecordList")
	private List<SignDetail> signRecordList;
	
	@XmlElement(name = "total")
    private long total;
	
	@XmlElement(name = "size")
    private long size;
	
	public List<SignDetail> getSignRecordList() {
		return signRecordList;
	}

	public void setSignRecordList(List<SignDetail> signRecordList) {
		this.signRecordList = signRecordList;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	
}
