package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.McDashboardPanel;
import com.fiberhome.mapps.servicemanager.entity.McDashboardPanelSeries;

public class PanelDetailResponse extends BaseResponse {
	private McDashboardPanel dashboardPanel;
	
	private List<McDashboardPanelSeries> dashboardPanelSeries;

	public McDashboardPanel getDashboardPanel() {
		return dashboardPanel;
	}

	public void setDashboardPanel(McDashboardPanel dashboardPanel) {
		this.dashboardPanel = dashboardPanel;
	}

	public List<McDashboardPanelSeries> getDashboardPanelSeries() {
		return dashboardPanelSeries;
	}

	public void setDashboardPanelSeries(List<McDashboardPanelSeries> dashboardPanelSeries) {
		this.dashboardPanelSeries = dashboardPanelSeries;
	}
}
