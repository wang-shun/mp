package com.fiberhome.mapps.servicemanager.entity;

import java.util.List;

import org.influxdb.dto.QueryResult.Series;

public class DisplayEchartsInfo {
	List<Series> series;
	
	McDashboardPanel panel;

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}

	public McDashboardPanel getPanel() {
		return panel;
	}

	public void setPanel(McDashboardPanel panel) {
		this.panel = panel;
	}

}