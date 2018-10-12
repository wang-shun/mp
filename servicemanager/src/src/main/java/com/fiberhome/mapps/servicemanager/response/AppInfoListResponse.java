package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.AppInfo;

public class AppInfoListResponse extends BaseResponse {
	List<AppInfo> list;
	
	List<AppInfo> offLineList;

	public List<AppInfo> getList() {
		return list;
	}

	public void setList(List<AppInfo> list) {
		this.list = list;
	}

	public List<AppInfo> getOffLineList() {
		return offLineList;
	}

	public void setOffLineList(List<AppInfo> offLineList) {
		this.offLineList = offLineList;
	}
}
