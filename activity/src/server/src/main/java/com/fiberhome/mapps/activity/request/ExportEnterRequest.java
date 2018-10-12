package com.fiberhome.mapps.activity.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class ExportEnterRequest extends AbstractRopRequest{
	@NotNull
    private String actId;
	
	private String email;
	
	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
