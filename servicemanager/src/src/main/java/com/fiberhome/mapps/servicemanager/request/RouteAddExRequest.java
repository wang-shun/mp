package com.fiberhome.mapps.servicemanager.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class RouteAddExRequest extends AbstractRopRequest {
	@NotNull
	private String url;
	
	private String serviceName;
	
	private String path;
	
	private String stripPrefix;
	
	private String retryable;
	
	private String ssHeaders;
	
	private String needAuth;
	
	private String enabled;
	
	private String authResource;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getStripPrefix() {
		return stripPrefix;
	}

	public void setStripPrefix(String stripPrefix) {
		this.stripPrefix = stripPrefix;
	}

	public String getRetryable() {
		return retryable;
	}

	public void setRetryable(String retryable) {
		this.retryable = retryable;
	}


	public String getNeedAuth() {
		return needAuth;
	}

	public void setNeedAuth(String needAuth) {
		this.needAuth = needAuth;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getSsHeaders() {
		return ssHeaders;
	}

	public void setSsHeaders(String ssHeaders) {
		this.ssHeaders = ssHeaders;
	}

	public String getAuthResource() {
		return authResource;
	}

	public void setAuthResource(String authResource) {
		this.authResource = authResource;
	}
	
}
