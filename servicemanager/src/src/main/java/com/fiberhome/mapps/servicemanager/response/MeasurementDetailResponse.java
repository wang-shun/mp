package com.fiberhome.mapps.servicemanager.response;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.McMeasurement;

public class MeasurementDetailResponse extends BaseResponse {
	McMeasurement measurementDetail;

	public McMeasurement getMeasurementDetail() {
		return measurementDetail;
	}

	public void setMeasurementDetail(McMeasurement measurementDetail) {
		this.measurementDetail = measurementDetail;
	}
}
