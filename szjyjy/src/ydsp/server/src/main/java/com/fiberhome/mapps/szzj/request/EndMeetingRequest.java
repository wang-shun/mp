package com.fiberhome.mapps.szzj.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class EndMeetingRequest extends AbstractRopRequest{
	@NotNull
	private String meetingId;
	
	@NotNull
	private String hostId;

	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
}
