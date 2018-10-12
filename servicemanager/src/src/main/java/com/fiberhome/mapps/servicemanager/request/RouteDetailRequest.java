package com.fiberhome.mapps.servicemanager.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class RouteDetailRequest extends AbstractRopRequest {
	@NotNull
    private String id;
	
	private String serviceId;
	
	private String url;
	
	private String serviceName;
	
	private String path;
	
	private String stripPrefix;
	
	private String retryable;
	
	private String sentitiveHeaders;
	
	private String needAuth;
	
	private String enabled;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

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

	public String getSentitiveHeaders() {
		return sentitiveHeaders;
	}

	public void setSentitiveHeaders(String sentitiveHeaders) {
		this.sentitiveHeaders = sentitiveHeaders;
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
	
}
