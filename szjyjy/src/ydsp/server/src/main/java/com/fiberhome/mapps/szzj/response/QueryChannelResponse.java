package com.fiberhome.mapps.szzj.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.szzj.entity.VChannel;
import com.fiberhome.mapps.szzj.entity.VChannelDetail;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryChannelResponse extends BaseResponse{

	@XmlElement(name = "channelList")
	private List<VChannelDetail> channelList;
	
    @XmlElement(name = "timestamp")
    private long            timestamp;

    @XmlElement(name = "endflag")
    private int             endflag;

    @XmlElement(name = "total")
    private long            total;

    @XmlElement(name = "reservedFlag")
    private int             reservedFlag;

    @XmlElement(name = "adminFlag")
    private int             adminFlag;

	public List<VChannelDetail> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<VChannelDetail> channelList) {
		this.channelList = channelList;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getEndflag() {
		return endflag;
	}

	public void setEndflag(int endflag) {
		this.endflag = endflag;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getReservedFlag() {
		return reservedFlag;
	}

	public void setReservedFlag(int reservedFlag) {
		this.reservedFlag = reservedFlag;
	}

	public int getAdminFlag() {
		return adminFlag;
	}

	public void setAdminFlag(int adminFlag) {
		this.adminFlag = adminFlag;
	}
		
}
