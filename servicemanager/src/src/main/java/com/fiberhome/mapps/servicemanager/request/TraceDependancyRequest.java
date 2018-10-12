package com.fiberhome.mapps.servicemanager.request;

import com.rop.AbstractRopRequest;

public class TraceDependancyRequest extends AbstractRopRequest {
	private String endTs;
	private String lookback;
	public String getEndTs() {
		return endTs;
	}
	public void setEndTs(String endTs) {
		this.endTs = endTs;
	}
	public String getLookback() {
		return lookback;
	}
	public void setLookback(String lookback) {
		this.lookback = lookback;
	}
}
