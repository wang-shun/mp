package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.McUnitWeight;

public class UnitWeightListResponse extends BaseResponse {
	List<McUnitWeight> unitList;

	public List<McUnitWeight> getUnitList() {
		return unitList;
	}

	public void setUnitList(List<McUnitWeight> unitList) {
		this.unitList = unitList;
	}
}
