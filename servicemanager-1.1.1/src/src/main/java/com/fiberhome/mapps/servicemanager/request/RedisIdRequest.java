package com.fiberhome.mapps.servicemanager.request;

import com.rop.AbstractRopRequest;

public class RedisIdRequest extends AbstractRopRequest {
	private String redisId;

	public String getRedisId() {
		return redisId;
	}

	public void setRedisId(String redisId) {
		this.redisId = redisId;
	}

}
