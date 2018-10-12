package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.Column;
import javax.persistence.Id;

public class ClientServiceInfo {
    @Id
    @Column(name = "svc_id")
    private String svcId;
    
    private String svcName;

    private String appkey;

    private String secret;
    
    @Column(name = "need_auth")
    private String needAuth;
    
    private String serviceUrl;
    
    private String path;

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

	public String getSvcName() {
		return svcName;
	}

	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}

	public String getNeedAuth() {
		return needAuth;
	}

	public void setNeedAuth(String needAuth) {
		this.needAuth = needAuth;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}