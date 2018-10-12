package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.mssdk.ConfigParam;
import com.fiberhome.mapps.mssdk.Resource;
import com.fiberhome.mapps.servicemanager.entity.ClientInstanceInfo;
import com.fiberhome.mapps.servicemanager.entity.ClientServiceInfo;

public class DependenciesResponse extends BaseResponse {
	private List<ClientServiceInfo> services;
	
	private List<Resource> resources;
	
	private String appName; //文件服务  
	
	private String logo; // /images/logo.png
	
	private String remarks; //描述    
	
	private String portal; // /websso
	
	private String mgrPort; // 管理端口 
	
	private String mgrContextPath; // 管理路径 
	
	private List<ConfigParam> configProperties; //配置参数
	
	private List<ClientInstanceInfo> instanceList; //实例列表

	public List<ClientServiceInfo> getServices() {
		return services;
	}

	public void setServices(List<ClientServiceInfo> services) {
		this.services = services;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
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

	public List<ConfigParam> getConfigProperties() {
		return configProperties;
	}

	public void setConfigProperties(List<ConfigParam> configProperties) {
		this.configProperties = configProperties;
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

	public List<ClientInstanceInfo> getInstanceList() {
		return instanceList;
	}

	public void setInstanceList(List<ClientInstanceInfo> instanceList) {
		this.instanceList = instanceList;
	}
}
