package com.fiberhome.mapps.servicemanager.request;

import com.rop.AbstractRopRequest;

public class ResourceInfoSaveRequest extends AbstractRopRequest {
	private String id;
	
	private String name;
	
	private String resId;

	private String configList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getConfigList() {
		return configList;
	}

	public void setConfigList(String configList) {
		this.configList = configList;
	}
}
