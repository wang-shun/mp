package com.fiberhome.mapps.servicemanager.response;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.ClientDatabaseInfo;
import com.fiberhome.mapps.servicemanager.entity.ClientRedisInfo;

public class ResourceListResponse extends BaseResponse {
	List<ClientRedisInfo> redisList;

	List<ClientDatabaseInfo> databases;
	
	Map<String, Object> resMap;

	public List<ClientRedisInfo> getRedisList() {
		return redisList;
	}

	public void setRedisList(List<ClientRedisInfo> redisList) {
		this.redisList = redisList;
	}

	public List<ClientDatabaseInfo> getDatabases() {
		return databases;
	}

	public void setDatabases(List<ClientDatabaseInfo> databases) {
		this.databases = databases;
	}

	public Map<String, Object> getResMap() {
		return resMap;
	}

	public void setResMap(Map<String, Object> resMap) {
		this.resMap = resMap;
	}
}
