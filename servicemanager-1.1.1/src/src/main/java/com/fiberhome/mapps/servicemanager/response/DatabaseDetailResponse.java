package com.fiberhome.mapps.servicemanager.response;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.SmDatabase;

public class DatabaseDetailResponse extends BaseResponse {
	SmDatabase databaseDetail;

	public SmDatabase getDatabaseDetail() {
		return databaseDetail;
	}

	public void setDatabaseDetail(SmDatabase databaseDetail) {
		this.databaseDetail = databaseDetail;
	}
}
