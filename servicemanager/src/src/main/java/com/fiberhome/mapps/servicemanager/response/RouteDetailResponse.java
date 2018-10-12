package com.fiberhome.mapps.servicemanager.response;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.SmRoute;

public class RouteDetailResponse extends BaseResponse {
	SmRoute routeDetail;

	public SmRoute getRouteDetail() {
		return routeDetail;
	}

	public void setRouteDetail(SmRoute routeDetail) {
		this.routeDetail = routeDetail;
	}

}
