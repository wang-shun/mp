package com.fiberhome.mapps.servicemanager.request;

import com.rop.AbstractRopRequest;

public class DisplayEchartsRequest extends AbstractRopRequest {
	private String idList;
	
	private String refreshTime;
	
	private String timeRange;

	public String getIdList() {
		return idList;
	}

	public void setIdList(String idList) {
		this.idList = idList;
	}

	public String getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(String refreshTime) {
		this.refreshTime = refreshTime;
	}

	public String getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(String timeRange) {
		this.timeRange = timeRange;
	}
}
