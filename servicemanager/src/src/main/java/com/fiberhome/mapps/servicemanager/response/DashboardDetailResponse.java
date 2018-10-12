package com.fiberhome.mapps.servicemanager.response;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.McDashboard;

public class DashboardDetailResponse extends BaseResponse {
	McDashboard dashboard;

	public McDashboard getDashboard() {
		return dashboard;
	}

	public void setDashboard(McDashboard dashboard) {
		this.dashboard = dashboard;
	}
}
