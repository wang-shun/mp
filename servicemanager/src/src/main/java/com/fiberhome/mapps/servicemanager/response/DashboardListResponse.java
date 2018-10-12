package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.McDashboard;

public class DashboardListResponse extends BaseResponse {
	List<McDashboard> dashboardList;
	
	long total;

	public List<McDashboard> getDashboardList() {
		return dashboardList;
	}

	public void setDashboardList(List<McDashboard> dashboardList) {
		this.dashboardList = dashboardList;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}
