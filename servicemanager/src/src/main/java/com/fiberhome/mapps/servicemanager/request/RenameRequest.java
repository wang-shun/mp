package com.fiberhome.mapps.servicemanager.request;

import com.rop.AbstractRopRequest;

public class RenameRequest extends AbstractRopRequest {
	private String id;
	
	private String name;

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
}
