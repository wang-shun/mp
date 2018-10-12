package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.ClientSvcAuthInfo;

public class SVCAuthGetResponse extends BaseResponse {
	List<ClientSvcAuthInfo> list;

	public List<ClientSvcAuthInfo> getList() {
		return list;
	}

	public void setList(List<ClientSvcAuthInfo> list) {
		this.list = list;
	}


}
