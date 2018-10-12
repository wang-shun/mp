package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.RsKeyValue;

public class AppConfigResponse extends BaseResponse {
	List<RsKeyValue> config;

	public List<RsKeyValue> getConfig() {
		return config;
	}

	public void setConfig(List<RsKeyValue> config) {
		this.config = config;
	}

}
