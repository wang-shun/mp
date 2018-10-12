package com.fiberhome.mapps.servicemanager.request;

import com.fiberhome.mapps.servicemanager.entity.McDashboard;
import com.rop.AbstractRopRequest;

public class DashboardSaveRequest extends AbstractRopRequest {
	private String dashboardSaveJson;
	
	private McDashboard dashboard;

	public String getDashboardSaveJson() {
		return dashboardSaveJson;
	}

	public void setDashboardSaveJson(String dashboardSaveJson) {
		this.dashboardSaveJson = dashboardSaveJson;
	}

	public McDashboard getDashboard() {
		return dashboard;
	}

	public void setDashboard(McDashboard dashboard) {
		this.dashboard = dashboard;
	}
	

}
