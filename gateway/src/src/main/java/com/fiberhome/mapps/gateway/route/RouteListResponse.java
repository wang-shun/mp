package com.fiberhome.mapps.gateway.route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.StringUtils;

public class RouteListResponse  {
	String code;
	
	String message;
	
	String version;
	
	List<RouteEx> routes;

	public List<RouteEx> getRoutes() {
		return routes;
	}

	public void setRoutes(List<RouteEx> routes) {
		this.routes = routes;
		
		List<String> sign = new ArrayList<String>();
		for (RouteEx route : routes) {
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
