package com.fiberhome.mapps.gateway.security;

public class ServiceAuthNotifyMessage {
	private String appId;
	private String svcId;
	private String action;
	
	public ServiceAuthNotifyMessage() {
		
	}
	
	public ServiceAuthNotifyMessage(String appId, String svcId) {
		this(appId, svcId, "assign");
	}
	
	public ServiceAuthNotifyMessage(String appId, String svcId, String action) {
		this.appId = appId;
		this.svcId = svcId;
		this.action = action;
	}
	
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
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	
}
