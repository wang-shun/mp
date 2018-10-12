package com.fiberhome.mapps.servicemanager.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class AppSaveFormRequest extends AbstractRopRequest {
	
	@NotNull
	private String appId;
	
	//json
	@NotNull
	private String config;
	
	// json
	@NotNull
	private String resourceList;
	
	//json
	@NotNull
	private String serviceList;
	
	@NotNull
	private boolean configChange;
	
	@NotNull
	private boolean resourceChange;
	
	@NotNull
	private boolean serviceChange;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getResourceList() {
		return resourceList;
	}

	public void setResourceList(String resourceList) {
		this.resourceList = resourceList;
	}

	public String getServiceList() {
		return serviceList;
	}

	public void setServiceList(String serviceList) {
		this.serviceList = serviceList;
	}

	public boolean isConfigChange() {
		return configChange;
	}

	public void setConfigChange(boolean configChange) {
		this.configChange = configChange;
	}

	public boolean isResourceChange() {
		return resourceChange;
	}

	public void setResourceChange(boolean resourceChange) {
		this.resourceChange = resourceChange;
	}

	public boolean isServiceChange() {
		return serviceChange;
	}

	public void setServiceChange(boolean serviceChange) {
		this.serviceChange = serviceChange;
	}
}
