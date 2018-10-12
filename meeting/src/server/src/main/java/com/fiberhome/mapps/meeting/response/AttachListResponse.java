package com.fiberhome.mapps.meeting.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.meeting.entity.MtAttachment;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class AttachListResponse extends BaseResponse{

	
	@XmlElement(name = "attachList")
	private List<MtAttachment>  attachList;
	
	@XmlElement(name = "total")
    private long                    total;
	
	public List<MtAttachment> getAttachList() {
		return attachList;
	}

	public void setAttachList(List<MtAttachment> attachList) {
		this.attachList = attachList;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
	
}
