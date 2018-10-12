package com.fiberhome.mapps.szzj.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class DetailVChannelResponse extends BaseResponse{
	@XmlElement(name = "hostId")
	private String hostId;
	
	@XmlElement(name = "channel")
	private String channel;
	
	@XmlElement(name = "hostPass")
	private String hostPass;
	
	@XmlElement(name = "hostEmail")
	private String hostEmail;
	
	@XmlElement(name = "capacity")
	private Integer capacity;

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getHostPass() {
		return hostPass;
	}

	public void setHostPass(String hostPass) {
		this.hostPass = hostPass;
	}

	public String getHostEmail() {
		return hostEmail;
	}

	public void setHostEmail(String hostEmail) {
		this.hostEmail = hostEmail;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

}
