package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.Column;
import javax.persistence.Id;

public class ClientSvcAuthInfo {
    @Id
    @Column(name = "app_id")
    private String appId;

    @Id
    @Column(name = "svc_id")
    private String svcId;

    private String appkey;

    private String secret;

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

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}