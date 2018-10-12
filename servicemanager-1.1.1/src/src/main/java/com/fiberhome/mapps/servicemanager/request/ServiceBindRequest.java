package com.fiberhome.mapps.servicemanager.request;

import com.rop.AbstractRopRequest;

public class ServiceBindRequest extends AbstractRopRequest {
	private String svcId;
	
	// json
	private String config;

	public String getSvcId() {
		return svcId;
	}

	public void setSvcId(String svcId) {
		this.svcId = svcId;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}
	
	
}
