package com.fiberhome.mapps.servicemanager.request;

import com.rop.AbstractRopRequest;

public class DatabaseDetailRequest extends AbstractRopRequest {
	private String databaseId;

	public String getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}
	
}
