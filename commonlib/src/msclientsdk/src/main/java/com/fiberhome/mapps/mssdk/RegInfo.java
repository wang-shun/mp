package com.fiberhome.mapps.mssdk;

import java.util.List;

public class RegInfo {
	private List<ConfigParam> configProperties;

	private String[] services;

	private List<Resource> resources;
	
	private String homePageUrl;
	
	private String statusPageUrl;
	
	private String healthCheckUrl;
	
	private String appName; //文件服务  
	
	private String logo; // /images/logo.png
	
	private String remarks; //描述    
	
	private String portal; // /websso 
	
	private String mgrPort; // 管理端口 
	
	private String mgrContextPath; // 管理路径 

	public List<ConfigParam> getConfigProperties() {
		return configProperties;
	}

	public void setConfigProperties(List<ConfigParam> configProperties) {
		this.configProperties = configProperties;
	}

	public String[] getServices() {
		return services;
	}

	public void setServices(String[] services) {
		this.services = services;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public String getHomePageUrl() {
		return homePageUrl;
	}

	public void setHomePageUrl(String homePageUrl) {
		this.homePageUrl = homePageUrl;
	}

	public String getStatusPageUrl() {
		return statusPageUrl;
	}

	public void setStatusPageUrl(String statusPageUrl) {
		this.statusPageUrl = statusPageUrl;
	}

	public String getHealthCheckUrl() {
		return healthCheckUrl;
	}

	public void setHealthCheckUrl(String healthCheckUrl) {
		this.healthCheckUrl = healthCheckUrl;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPortal() {
		return portal;
	}

	public void setPortal(String portal) {
		this.portal = portal;
	}

	public String getMgrPort() {
		return mgrPort;
	}

	public void setMgrPort(String mgrPort) {
		this.mgrPort = mgrPort;
	}

	public String getMgrContextPath() {
		return mgrContextPath;
	}

	public void setMgrContextPath(String mgrContextPath) {
		this.mgrContextPath = mgrContextPath;
	}

}
