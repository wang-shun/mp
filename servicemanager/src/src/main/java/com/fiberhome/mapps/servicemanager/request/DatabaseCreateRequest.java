package com.fiberhome.mapps.servicemanager.request;

import com.fiberhome.mapps.servicemanager.entity.SmDatabase;
import com.rop.AbstractRopRequest;

public class DatabaseCreateRequest extends AbstractRopRequest {
	private String databaseJson;
	private SmDatabase database;
	private String sidordbname;
	private String adminuser;
	private String adminpass;
	
	public String getAdminuser() {
		return adminuser;
	}

	public void setAdminuser(String adminuser) {
		this.adminuser = adminuser;
	}

	public String getAdminpass() {
		return adminpass;
	}

	public void setAdminpass(String adminpass) {
		this.adminpass = adminpass;
	}

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
