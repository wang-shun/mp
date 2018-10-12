package com.fiberhome.mapps.servicemanager.request;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.rop.AbstractRopRequest;

public class SVCAuthAssignRequest extends AbstractRopRequest {
	
	@NotNull
	private String appId;
	
	//json
	@NotNull
	private String serviceList;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getServiceList() {
		return serviceList;
	}

	public void setServiceList(String serviceList) {
		this.serviceList = serviceList;
	}
}
