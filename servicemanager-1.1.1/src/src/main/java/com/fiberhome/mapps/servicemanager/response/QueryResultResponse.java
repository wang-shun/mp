package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import org.influxdb.dto.QueryResult.Result;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

public class QueryResultResponse extends BaseResponse {
	List<Result> queryResult;

	public List<Result> getQueryResult() {
		return queryResult;
	}

	public void setQueryResult(List<Result> queryResult) {
		this.queryResult = queryResult;
	}
	
}
