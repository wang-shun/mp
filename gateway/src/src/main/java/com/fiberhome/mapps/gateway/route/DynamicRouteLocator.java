package com.fiberhome.mapps.gateway.route;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.util.StringUtils;

import com.fiberhome.mapps.gateway.utils.EurekaClientUtils;
import com.fiberhome.mos.core.openapi.rop.client.RopClient;
import com.fiberhome.mos.core.openapi.rop.client.RopClientException;

/**
 * 继承之DiscoveryClientRouteLocator，用于动态路由的实现，通过事件触发路由的升级
 * @author fh
 *
 */
public class DynamicRouteLocator extends DiscoveryClientRouteLocator {
	
	public static final String DEFAULT_ROUTE = "/**";
	
	private static Logger LOG = LoggerFactory.getLogger(DynamicRouteLocator.class);

	private ZuulProperties properties;
	
	DiscoveryClient discovery;
	
	RouteCache routeCache;
	
	public DynamicRouteLocator(String servletPath, ZuulProperties properties, DiscoveryClient discovery, RouteCache routeCache) {
		super(servletPath, discovery, properties);
		this.properties = properties;
		this.discovery = discovery;
		this.routeCache = routeCache;
	}

	public void addRoute(String path, String location) {
		this.properties.getRoutes().put(path, new ZuulRoute(path, location));
		refresh();
	}

	public void addRoute(ZuulRoute route) {
		this.properties.getRoutes().put(route.getPath(), route);
		refresh();
	}
	
	public void resetRoute(Map<String, ZuulRoute> routes) {
		this.properties.getRoutes().clear();
		addConfiguredRoutes(routes);
	}
	
	/**
	 * 判断路由URI是否需要鉴权 
	 * @param requestURI
	 * @return
	 */
	public boolean isNeedAuth(String requestURI) {
		Route route = this.getMatchingRoute(requestURI);
		
		if (route == null) return false;
		
		RouteEx routex = routeCache.get(route.getLocation());		
		
		return routex != null && routex.needAuth(route.getPath());
	}

	@Override
	protected LinkedHashMap<String, ZuulRoute> locateRoutes() {
		LinkedHashMap<String, ZuulRoute> routesMap = new LinkedHashMap<String, ZuulRoute>();
		for (ZuulRoute route : this.properties.getRoutes().values()) {
			routesMap.put(route.getPath(), route);
		}
		
		if (routesMap.get(DEFAULT_ROUTE) != null) {
			ZuulRoute defaultRoute = routesMap.get(DEFAULT_ROUTE);
			// Move the defaultServiceId to the end
			routesMap.remove(DEFAULT_ROUTE);
			routesMap.put(DEFAULT_ROUTE, defaultRoute);
		}
		LinkedHashMap<String, ZuulRoute> values = new LinkedHashMap<>();
		for (Entry<String, ZuulRoute> entry : routesMap.entrySet()) {
			String path = entry.getKey();
			// Prepend with slash if not already present.
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			if (StringUtils.hasText(this.properties.getPrefix())) {
				path = this.properties.getPrefix() + path;
				if (!path.startsWith("/")) {
					path = "/" + path;
				}
			}
			values.put(path, entry.getValue());
		}
		return values;
	}
	
	public void refresh() {
		doRefresh();
		routeUpdate();
	}
	
	public void routeUpdate() {
		if (routeCache != null) {
			routeCache.clear();
		}
	
		EurekaClientUtils clientUtils = new EurekaClientUtils(discovery);
		try {
			RopClient client = clientUtils.getServiceManagerClient();
    		HashMap<String, Object> param = new HashMap<String, Object>();
    		
    		param.put("v", "1.0");
    		param.put("offset", 1);
    		param.put("limit", 1000);
    		
    		try {
    			RouteListResponse response = client.requestForObject("mapps.servicemanager.route.query", param, RouteListResponse.class);
    			
    			if ("1".equals(response.getCode())) {
    				LOG.debug("获取路由列表：{}", response.getRoutes());
	    			this.properties.getRoutes().clear();
	    			for (RouteEx route : response.getRoutes()) {
	    				route.setPath(route.getPath());
	    				if (!route.isEnabled() || route.getServiceId().equalsIgnoreCase("mapps-servicemanager")) continue;
	    				
	    				this.properties.getRoutes().put(route.getPath(), route);
	    				routeCache.put(route.getLocation(), route);
	    				LOG.debug("route {} -> {}", route.getPath(), route.getLocation());
	    			}
	    			doRefresh();
    			} else {
    				LOG.warn("请求路由列表失败，错误信息：{}/{}", response.getCode(), response.getMessage());
    			}
			} catch (RopClientException e) {
				LOG.error("获取路由失败, 请检查ServiceManager接口mapps.servicemanager.route.list服务是否正常。", e);
			}
		} catch (Exception ex) {
			LOG.warn("没有可用的服务管理节点，不能获取到服务列表。", ex);
		}
    }
	
	protected void addConfiguredRoutes(Map<String, ZuulRoute> routes) {
		Map<String, ZuulRoute> routeEntries = this.properties.getRoutes();
		for (ZuulRoute entry : routeEntries.values()) {
			String route = entry.getPath();
			if (routes.containsKey(route)) {
				LOG.warn("Overwriting route " + route + ": already defined by "
						+ routes.get(route));
			}
			routes.put(route, entry);
		}
	}
	
	private String getRouteLocation(String requestURI) {
		Route route = this.getMatchingRoute(requestURI);
		
		return route != null ? route.getLocation() : null;
	}
	
	
}
