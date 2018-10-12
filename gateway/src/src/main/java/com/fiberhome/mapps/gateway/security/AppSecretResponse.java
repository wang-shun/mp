package com.fiberhome.mapps.gateway.security;

import java.util.List;

public class AppSecretResponse {
	private List<AppSecretInfo> list;
	
	public AppSecretResponse() {
		
	}

	public List<AppSecretInfo> getList() {
		return list;
	}

	public void setList(List<AppSecretInfo> list) {
		this.list = list;
	}	
}
