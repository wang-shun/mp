package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.ClientServiceInfo;
import com.fiberhome.mapps.servicemanager.entity.ResourceAssignInfo;
import com.fiberhome.mapps.servicemanager.entity.RsKeyValue;

public class SVCConfigResponse extends BaseResponse {
	List<ClientServiceInfo> services;
	
	List<RsKeyValue> config;

	List<ResourceAssignInfo> resources;
	
	public List<ClientServiceInfo> getServices() {
		return services;
	}

	public void setServices(List<ClientServiceInfo> services) {
		this.services = services;
	}

	public List<RsKeyValue> getConfig() {
		return config;
	}

	public void setConfig(List<RsKeyValue> config) {
		this.config = config;
	}

	public List<ResourceAssignInfo> getResources() {
		return resources;
	}

	public void setResources(List<ResourceAssignInfo> resources) {
		this.resources = resources;
	}
}
