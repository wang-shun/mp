package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.RsKeyValue;

public class KapcitorFilterResponse extends BaseResponse {
	List<RsKeyValue> filterList;

	public List<RsKeyValue> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<RsKeyValue> filterList) {
		this.filterList = filterList;
	}
}
