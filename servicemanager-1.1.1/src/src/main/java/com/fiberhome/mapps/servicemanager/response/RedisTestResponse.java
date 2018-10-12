package com.fiberhome.mapps.servicemanager.response;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.SmDatabase;

public class RedisTestResponse extends BaseResponse{
	String redisTestResult;

	public String getRedisTestResult() {
		return redisTestResult;
	}

	public void setRedisTestResult(String redisTestResult) {
		this.redisTestResult = redisTestResult;
	}
}
