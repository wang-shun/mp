package com.fiberhome.mapps.szzj.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class AddVChannelRequest extends AbstractRopRequest{
	
	private String channel;
	@NotNull
	private String hostId;
	
	private String hostPass;
	
	private String hostEmail;
	
	private Integer capacity;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
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
