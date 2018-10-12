package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.SmResourceInfo;

public class ResourceTypeResponse extends BaseResponse {
	List<SmResourceInfo> resourceType;

	public List<SmResourceInfo> getResourceType() {
		return resourceType;
	}

	public void setResourceType(List<SmResourceInfo> resourceType) {
		this.resourceType = resourceType;
	}

}
