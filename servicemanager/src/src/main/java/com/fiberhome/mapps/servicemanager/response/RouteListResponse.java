package com.fiberhome.mapps.servicemanager.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.flywaydb.core.internal.util.StringUtils;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

public class RouteListResponse extends BaseResponse {
	String version;
	
	List<ZuulRoute> routes;
	
    long total;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<ZuulRoute> getRoutes() {
		return routes;
	}

	public void setRoutes(List<ZuulRoute> routes) {
		this.routes = routes;
		
		List<String> sign = new ArrayList<String>();
		for (ZuulRoute route : routes) {
			sign.add(route.getPath() + "^" + route.getLocation());
		}
		Collections.sort(sign);
		
		version = "V" + StringUtils.collectionToCommaDelimitedString(sign).hashCode();
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
