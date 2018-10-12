package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.ClientResourceInfo;

public class ResourceInfoListResponse extends BaseResponse {
	List<ClientResourceInfo> resourceList;

	public List<ClientResourceInfo> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<ClientResourceInfo> resourceList) {
		this.resourceList = resourceList;
	}

	long total;


	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}
