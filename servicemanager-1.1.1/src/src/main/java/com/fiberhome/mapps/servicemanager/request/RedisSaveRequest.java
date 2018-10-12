package com.fiberhome.mapps.servicemanager.request;

import com.fiberhome.mapps.servicemanager.entity.SmRedis;
import com.rop.AbstractRopRequest;

public class RedisSaveRequest extends AbstractRopRequest {
	private String redisJson;

	private SmRedis redis;

	public String getRedisJson() {
		return redisJson;
	}

	public void setRedisJson(String redisJson) {
		this.redisJson = redisJson;
	}

	public SmRedis getRedis() {
		return redis;
	}

	public void setRedis(SmRedis redis) {
		this.redis = redis;
	}

}
