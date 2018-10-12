package com.fiberhome.mapps.gateway.security.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
/**
 * Zuul Filter 介绍：<a>https://github.com/Netflix/zuul/wiki/How-it-Works</a>
 */
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.web.util.UrlPathHelper;

import com.fiberhome.mapps.gateway.route.DynamicRouteLocator;
import com.fiberhome.mapps.gateway.security.AppSecretManager;
import com.netflix.util.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;

public class CookiePathFilter extends ZuulFilter implements FilterConstants{
	private final static Logger LOG = LoggerFactory.getLogger(CookiePathFilter.class);

	private DynamicRouteLocator routeLocator;

	private ZuulProperties properties;

	private UrlPathHelper urlPathHelper = new UrlPathHelper();

	private ProxyRequestHelper proxyRequestHelper;;

	private AppSecretManager appSecretManager;

	public CookiePathFilter(DynamicRouteLocator routeLocator, ZuulProperties properties, ProxyRequestHelper proxyRequestHelper,
			AppSecretManager asm) {
		this.routeLocator = routeLocator;
		this.properties = properties;
		this.urlPathHelper.setRemoveSemicolonContent(properties.isRemoveSemicolonContent());
		this.proxyRequestHelper = proxyRequestHelper;
		this.appSecretManager = asm;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletResponse servletResponse = context.getResponse();
		
		List<Pair<String, String>> zuulResponseHeaders = context.getZuulResponseHeaders();
		if (zuulResponseHeaders != null) {
			for (Pair<String, String> it : zuulResponseHeaders) {
				if ("Set-Cookie".equalsIgnoreCase(it.first())){
					String cookieValue = it.second();
					
					if (cookieValue != null) {
						HttpServletRequest request = context.getRequest();
						if (request instanceof HttpServletRequestWrapper) {
							request = ((HttpServletRequestWrapper)request).getRequest();
						}
						
						String requestURI = this.urlPathHelper.getPathWithinApplication(request);
						Route route = this.routeLocator.getMatchingRoute(requestURI);
						
						String path = route.getPrefix() + "/";
					
						it.setSecond(cookieValue.replaceAll("Path=[^\\s]*;", "Path="+path+";"));
					}
				}
			}
		}
		
		return null;
	}

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return SEND_RESPONSE_FILTER_ORDER - 1;
	}

	public static void main(String[] args) {
		String cookie = "JSESSIONID=F18161BEFCC3EADB09422D1F4DC902B8; Path=/; HttpOnly";
		
		System.out.println(cookie.replaceAll("Path=[^\\s]*;", "Path=/mr/;"));
	}
}
