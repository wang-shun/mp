package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.ClientDatabaseInfo;

public class DatabaseListResponse extends BaseResponse {
	List<ClientDatabaseInfo> databases;
    public List<ClientDatabaseInfo> getDatabases() {
		return databases;
	}

	public void setDatabases(List<ClientDatabaseInfo> databases) {
		this.databases = databases;
	}

	long total;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
