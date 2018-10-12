package com.fiberhome.mapps.servicemanager.request;

import com.rop.AbstractRopRequest;

public class SVCAuthGetRequest extends AbstractRopRequest {
	
	private String appId;
	
	private String svcId;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSvcId() {
		return svcId;
	}

	public void setSvcId(String svcId) {
		this.svcId = svcId;
	}
	
	
}
