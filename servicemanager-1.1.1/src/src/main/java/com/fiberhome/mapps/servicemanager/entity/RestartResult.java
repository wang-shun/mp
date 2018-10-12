package com.fiberhome.mapps.servicemanager.entity;

public class RestartResult {
	boolean result;
	
	String ipPort;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getIpPort() {
		return ipPort;
	}

	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}
}
