package com.fiberhome.mapps.gateway.route;

import java.util.List;

import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.google.common.collect.Lists;

public class RouteEx extends ZuulRoute {
	private PathMatcher pathMatcher = new AntPathMatcher();
	
	private boolean needAuth = true;
	
	private String serviceName;
	
	private boolean enabled;
	
	private String authResource;
	
	private List<String> authResourceList = Lists.newArrayList();
	
	public boolean isNeedAuth() {
		return needAuth;
	}

	public void setNeedAuth(boolean needAuth) {
		this.needAuth = needAuth;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAuthResource() {
		return authResource;
	}

	public void setAuthResource(String authResource) {
		this.authResource = authResource;
		if (authResource != null) {
			String[] resources = authResource.split(",");
			for (String res : resources) {
				authResourceList.add(res.trim());
			}
		}
	}
	
	public List<String> getAuthResourceList() {
		return authResourceList;
	}
	
	public boolean needAuth(String path) {
		if (!needAuth) return false;
		
		for (String res : getAuthResourceList()) {
			if (this.pathMatcher.match(res, path)) return true;
		}
		return false;
	}
}
