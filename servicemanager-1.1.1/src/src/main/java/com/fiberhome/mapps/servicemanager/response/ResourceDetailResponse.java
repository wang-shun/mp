package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.RsKeyValue;
import com.fiberhome.mapps.servicemanager.entity.SmResource;

public class ResourceDetailResponse extends BaseResponse {
	SmResource resourceDetail;
	
	List<RsKeyValue> configList;

	public SmResource getResourceDetail() {
		return resourceDetail;
	}

	public void setResourceDetail(SmResource resourceDetail) {
		this.resourceDetail = resourceDetail;
	}

	public List<RsKeyValue> getConfigList() {
		return configList;
	}

	public void setConfigList(List<RsKeyValue> configList) {
		this.configList = configList;
	}

}
