package com.fiberhome.mapps.servicemanager.request;

import com.fiberhome.mapps.servicemanager.entity.SmDatabase;
import com.rop.AbstractRopRequest;

public class DatabaseSaveRequest extends AbstractRopRequest {
	private String databaseJson;
	private SmDatabase database;
	private String sidordbname;
	
	public String getSidordbname() {
		return sidordbname;
	}

	public void setSidordbname(String sidordbname) {
		this.sidordbname = sidordbname;
	}

	public SmDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SmDatabase database) {
		this.database = database;
	}

	public String getDatabaseJson() {
		return databaseJson;
	}

	public void setDatabaseJson(String databaseJson) {
		this.databaseJson = databaseJson;
	}
}
