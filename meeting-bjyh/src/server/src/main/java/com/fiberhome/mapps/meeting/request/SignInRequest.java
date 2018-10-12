package com.fiberhome.mapps.meeting.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class SignInRequest extends AbstractRopRequest{
	
	@NotNull
	private String participantId;
	
	@NotNull
    private String sequId;

	public String getSequId() {
		return sequId;
	}

	public void setSequId(String sequId) {
		this.sequId = sequId;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
	

}
