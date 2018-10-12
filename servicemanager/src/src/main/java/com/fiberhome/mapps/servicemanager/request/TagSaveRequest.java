package com.fiberhome.mapps.servicemanager.request;

import com.rop.AbstractRopRequest;

public class TagSaveRequest extends AbstractRopRequest {
	private String id;
	
	private String name;
	
	private String tag;
	
	private String systemId;

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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
}
