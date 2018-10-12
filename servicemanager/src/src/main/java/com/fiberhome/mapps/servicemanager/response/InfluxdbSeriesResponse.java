package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import org.influxdb.dto.QueryResult.Series;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

public class InfluxdbSeriesResponse extends BaseResponse {
	List<Series> series;

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}
}
