package com.fiberhome.mapps.servicemanager.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class ResourceAssignRequest extends AbstractRopRequest {
	@NotNull
	private String appId;
	
	@NotNull
	private String resId;
	
	@NotNull
	private String resCode;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
}
