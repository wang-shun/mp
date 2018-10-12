package com.fiberhome.mapps.servicemanager.entity;

import com.netflix.appinfo.InstanceInfo.InstanceStatus;

public class ClientInstanceInfo {
	private String id;
	
	private String host;
	
	private int port;
	
	private InstanceStatus status;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int i) {
		this.port = i;
	}

	public InstanceStatus getStatus() {
		return status;
	}

	public void setStatus(InstanceStatus instanceStatus) {
		this.status = instanceStatus;
	}
	
}