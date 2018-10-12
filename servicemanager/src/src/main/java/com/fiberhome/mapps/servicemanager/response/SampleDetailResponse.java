package com.fiberhome.mapps.servicemanager.response;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.McDownsample;

public class SampleDetailResponse extends BaseResponse {
	McDownsample sampleDetail;

	public McDownsample getSampleDetail() {
		return sampleDetail;
	}

	public void setSampleDetail(McDownsample sampleDetail) {
		this.sampleDetail = sampleDetail;
	}


}
