package com.fiberhome.mapps.servicemanager.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class ResourceSetupRequest extends AbstractRopRequest {
	
	@NotNull
	private String appId;
	
	// json
	@NotNull
	private String resourceList;
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getResourceList() {
		return resourceList;
	}

	public void setResourceList(String resourceList) {
		this.resourceList = resourceList;
	}
}
