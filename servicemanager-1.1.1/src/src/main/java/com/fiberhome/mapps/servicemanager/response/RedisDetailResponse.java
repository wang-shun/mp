package com.fiberhome.mapps.servicemanager.response;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.SmRedis;

public class RedisDetailResponse extends BaseResponse {
	SmRedis redisDetail;

	public SmRedis getRedisDetail() {
		return redisDetail;
	}

	public void setRedisDetail(SmRedis redisDetail) {
		this.redisDetail = redisDetail;
	}
}
