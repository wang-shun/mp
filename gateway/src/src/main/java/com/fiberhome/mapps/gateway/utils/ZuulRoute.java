package com.fiberhome.mapps.gateway.utils;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.util.StringUtils;

public class ZuulRoute {

	/**
	 * The ID of the route (the same as its map key by default).
	 */
	private String id;

	/**
	 * The path (pattern) for the route, e.g. /foo/**.
	 */
	private String path;

	/**
	 * The service ID (if any) to map to this route. You can specify a physical URL or
	 * a service, but not both.
	 */
	private String serviceId;

	/**
	 * A full physical URL to map to the route. An alternative is to use a service ID
	 * and service discovery to find the physical address.
	 */
	private String url;

	/**
	 * Flag to determine whether the prefix for this route (the path, minus pattern
	 * patcher) should be stripped before forwarding.
	 */
	private boolean stripPrefix = true;

	/**
	 * Flag to indicate that this route should be retryable (if supported). Generally
	 * retry requires a service ID and ribbon.
	 */
	private Boolean retryable;

	/**
	 * List of sensitive headers that are not passed to downstream requests. Defaults
	 * to a "safe" set of headers that commonly contain user credentials. It's OK to
	 * remove those from the list if the downstream service is part of the same system
	 * as the proxy, so they are sharing authentication data. If using a physical URL
	 * outside your own domain, then generally it would be a bad idea to leak user
	 * credentials.
	 */
	private Set<String> sensitiveHeaders = new LinkedHashSet<>();

	private boolean customSensitiveHeaders = false;
	
	public ZuulRoute() {
		
	}

	public ZuulRoute(String id, String path, String serviceId, String url,
			boolean stripPrefix, Boolean retryable, Set<String> sensitiveHeaders) {
		this.id = id;
		this.path = path;
		this.serviceId = serviceId;
		this.url = url;
		this.stripPrefix = stripPrefix;
		this.retryable = retryable;
		this.sensitiveHeaders = sensitiveHeaders;
	}

	public ZuulRoute(String text) {
		String location = null;
		String path = text;
		if (text.contains("=")) {
			String[] values = StringUtils
					.trimArrayElements(StringUtils.split(text, "="));
			location = values[1];
			path = values[0];
		}
		this.id = extractId(path);
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		setLocation(location);
		this.path = path;
	}

	public ZuulRoute(String path, String location) {
		this.id = extractId(path);
		this.path = path;
		setLocation(location);
	}

	public String getLocation() {
		if (StringUtils.hasText(this.url)) {
			return this.url;
		}
		return this.serviceId;
	}

	public void setLocation(String location) {
		if (location != null
				&& (location.startsWith("http:") || location.startsWith("https:"))) {
			this.url = location;
		}
		else {
			this.serviceId = location;
		}
	}

	private String extractId(String path) {
		path = path.startsWith("/") ? path.substring(1) : path;
		path = path.replace("/*", "").replace("*", "");
		return path;
	}

	public Route getRoute(String prefix) {
		return new Route(this.id, this.path, getLocation(), prefix, this.retryable,
				isCustomSensitiveHeaders() ? this.sensitiveHeaders : null);
	}

	public void setSensitiveHeaders(Set<String> headers) {
		this.customSensitiveHeaders = true;
		this.sensitiveHeaders = new LinkedHashSet<>(headers);
	}

	public boolean isCustomSensitiveHeaders() {
		return this.customSensitiveHeaders;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public boolean isStripPrefix() {
		return stripPrefix;
	}

	public void setStripPrefix(boolean stripPrefix) {
		this.stripPrefix = stripPrefix;
	}

	public Boolean getRetryable() {
		return retryable;
	}

	public void setRetryable(Boolean retryable) {
		this.retryable = retryable;
	}

	public Set<String> getSensitiveHeaders() {
		return sensitiveHeaders;
	}

	public void setCustomSensitiveHeaders(boolean customSensitiveHeaders) {
		this.customSensitiveHeaders = customSensitiveHeaders;
	}
	
}