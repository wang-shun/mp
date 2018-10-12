package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.ClientRedisInfo;

public class RedisListResponse extends BaseResponse {
	List<ClientRedisInfo> redisList;

	long total;

	public List<ClientRedisInfo> getRedisList() {
		return redisList;
	}

	public void setRedisList(List<ClientRedisInfo> redisList) {
		this.redisList = redisList;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
