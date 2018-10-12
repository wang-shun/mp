package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.ResourceAssignInfo;

public class ResourceGetResponse extends BaseResponse {
	List<ResourceAssignInfo> resources;

	public List<ResourceAssignInfo> getResources() {
		return resources;
	}

	public void setResources(List<ResourceAssignInfo> resources) {
		this.resources = resources;
	}
}
