package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.McDownsample;

public class SampleQueryResponse extends BaseResponse {
	List<McDownsample> sampleList;
	
	long total;

	public List<McDownsample> getSampleList() {
		return sampleList;
	}

	public void setSampleList(List<McDownsample> sampleList) {
		this.sampleList = sampleList;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
