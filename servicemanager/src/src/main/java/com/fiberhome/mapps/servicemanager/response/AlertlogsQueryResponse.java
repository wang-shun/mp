package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.ClientAlertlogInfo;

public class AlertlogsQueryResponse extends BaseResponse {
	List<ClientAlertlogInfo> alertlogsList;
	
	long total;

	public List<ClientAlertlogInfo> getAlertlogsList() {
		return alertlogsList;
	}

	public void setAlertlogsList(List<ClientAlertlogInfo> alertlogsList) {
		this.alertlogsList = alertlogsList;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
