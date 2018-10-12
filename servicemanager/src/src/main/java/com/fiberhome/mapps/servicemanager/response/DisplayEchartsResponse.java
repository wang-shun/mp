package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.DisplayEchartsInfo;

public class DisplayEchartsResponse extends BaseResponse {
	private List<DisplayEchartsInfo> displayEchartsList;

	public List<DisplayEchartsInfo> getDisplayEchartsList() {
		return displayEchartsList;
	}

	public void setDisplayEchartsList(List<DisplayEchartsInfo> displayEchartsList) {
		this.displayEchartsList = displayEchartsList;
	}
}
