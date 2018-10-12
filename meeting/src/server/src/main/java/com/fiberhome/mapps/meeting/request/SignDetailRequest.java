package com.fiberhome.mapps.meeting.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class SignDetailRequest extends AbstractRopRequest{
	
	@NotNull
	private String meetingdId;
	
	@NotNull
    private String personId;

	public String getMeetingdId() {
		return meetingdId;
	}

	public void setMeetingdId(String meetingdId) {
		this.meetingdId = meetingdId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
}
