package com.fiberhome.mapps.servicemanager.request;

import com.rop.AbstractRopRequest;

public class ResourceListRequest extends AbstractRopRequest {
	private String resources;

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}
}
