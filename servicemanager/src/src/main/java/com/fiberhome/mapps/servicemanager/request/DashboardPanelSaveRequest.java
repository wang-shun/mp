package com.fiberhome.mapps.servicemanager.request;

import java.util.List;

import com.fiberhome.mapps.servicemanager.entity.McDashboardPanel;
import com.fiberhome.mapps.servicemanager.entity.McDashboardPanelSeries;
import com.rop.AbstractRopRequest;

public class DashboardPanelSaveRequest extends AbstractRopRequest {
	private String dashboardPanelSaveJson;
	
	private McDashboardPanel dashboardPanel;

	private List<McDashboardPanelSeries> dashboardPanelSeries;

	public String getDashboardPanelSaveJson() {
		return dashboardPanelSaveJson;
	}

	public void setDashboardPanelSaveJson(String dashboardPanelSaveJson) {
		this.dashboardPanelSaveJson = dashboardPanelSaveJson;
	}

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
