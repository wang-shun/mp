package com.fiberhome.mapps.servicemanager.response;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.SmDatabase;

public class DatabaseTestResponse extends BaseResponse{
	SmDatabase databaseTest;
	
	public SmDatabase getDatabaseDetail() {
		return databaseTest;
	}

	public void setDatabaseDetail(SmDatabase databaseDetail) {
		this.databaseTest = databaseDetail;
	}
}
