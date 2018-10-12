package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.AppRestartStatus;

public class AppRestartStatusResponse extends BaseResponse {
	List<AppRestartStatus> restartStatusList;

	public List<AppRestartStatus> getRestartStatusList() {
		return restartStatusList;
	}

	public void setRestartStatusList(List<AppRestartStatus> restartStatusList) {
		this.restartStatusList = restartStatusList;
	}

}
