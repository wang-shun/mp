package com.fiberhome.mapps.szzj.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class DeleteVChannelRequest extends AbstractRopRequest{
	@NotNull
	private String hostId;

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
}
