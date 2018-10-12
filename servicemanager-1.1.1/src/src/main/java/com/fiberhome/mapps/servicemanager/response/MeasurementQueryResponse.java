package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.McMeasurement;

public class MeasurementQueryResponse extends BaseResponse {
	List<McMeasurement> measurementList;
	
	long total;

	public List<McMeasurement> getMeasurementList() {
		return measurementList;
	}

	public void setMeasurementList(List<McMeasurement> measurementList) {
		this.measurementList = measurementList;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
