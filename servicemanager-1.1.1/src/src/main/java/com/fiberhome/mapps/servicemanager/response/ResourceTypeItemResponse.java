package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.SmResourceInfoitem;

public class ResourceTypeItemResponse extends BaseResponse {
	List<SmResourceInfoitem> resourceTypeItemList;

	public List<SmResourceInfoitem> getResourceTypeItemList() {
		return resourceTypeItemList;
	}

	public void setResourceTypeItemList(List<SmResourceInfoitem> resourceTypeItemList) {
		this.resourceTypeItemList = resourceTypeItemList;
	}
}
