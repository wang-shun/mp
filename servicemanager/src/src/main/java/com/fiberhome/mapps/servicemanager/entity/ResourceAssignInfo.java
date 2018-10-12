package com.fiberhome.mapps.servicemanager.entity;

import java.util.List;

public class ResourceAssignInfo {
	private String resId;
	
	private String resCode;
	
	private String id;
 
	private List<RsKeyValue> config;
	
	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<RsKeyValue> getConfig() {
		return config;
	}

	public void setConfig(List<RsKeyValue> config) {
		this.config = config;
	}
}
