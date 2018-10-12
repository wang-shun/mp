package com.fiberhome.mapps.gateway.security.filter;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
/**
 * Zuul Filter 介绍：<a>https://github.com/Netflix/zuul/wiki/How-it-Works</a>
 */
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.util.UrlPathHelper;

import com.fiberhome.mapps.gateway.route.DynamicRouteLocator;
import com.fiberhome.mapps.gateway.security.AppSecretManager;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;

public class AuthFilter extends ZuulFilter {
	private final static Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

	private DynamicRouteLocator routeLocator;

	private ZuulProperties properties;

	private UrlPathHelper urlPathHelper = new UrlPathHelper();

	private ProxyRequestHelper proxyRequestHelper;;

	private AppSecretManager appSecretManager;
	
	MultipartResolver multipartResolver;

	private Field requestField;

	public AuthFilter(DynamicRouteLocator routeLocator, ZuulProperties properties, ProxyRequestHelper proxyRequestHelper,
			AppSecretManager asm, MultipartResolver multipartResolver) {
		this.routeLocator = routeLocator;
		this.properties = properties;
		this.urlPathHelper.setRemoveSemicolonContent(properties.isRemoveSemicolonContent());
		this.proxyRequestHelper = proxyRequestHelper;
		this.appSecretManager = asm;
		
		this.multipartResolver = new CommonsMultipartResolver();
		
		this.requestField = ReflectionUtils.findField(HttpServletRequestWrapper.class,
				"req", HttpServletRequest.class);
		Assert.notNull(this.requestField,
				"HttpServletRequestWrapper.req field not found");
		this.requestField.setAccessible(true);
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		// 判断访问的服务是否为需要鉴权的服务
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		boolean isHttpServletRequestWrappered = false;
		if (request instanceof HttpServletRequestWrapper) {
			request = ((HttpServletRequestWrapper)request).getRequest();
			isHttpServletRequestWrappered = true;
		}
		final String requestURI = this.urlPathHelper.getPathWithinApplication(request);
		
		DynamicRouteLocator locator = (DynamicRouteLocator) this.routeLocator;

		Boolean needAuth = locator.isNeedAuth(this.proxyRequestHelper.buildZuulRequestURI(request));
		LOG.debug("{} need auth: {}", requestURI, needAuth);
		// 判断鉴权模式，access_token优先，appkey&secret为次
		if (needAuth) {
			boolean authorized = false;
			String accessToken = request.getParameter("access_token");

			if (accessToken != null) {
				authorized = appSecretManager.isValidate(accessToken);
			} else {	
				HttpServletRequest req = checkMultipart(request,isHttpServletRequestWrappered);
				authorized = appSecretManager.isValidate(req);
				if (!req.equals(request)) {
					ctx.setRequest(req);
				}
				LOG.info("鉴权结果：{}", authorized);
			}

			if (!authorized) {
				ctx.setResponseStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
				ctx.setSendZuulResponse(false);
				ctx.set("error.status_code", HttpServletResponse.SC_UNAUTHORIZED);
				ctx.set("error.message", "服务访问需要鉴权");
			}
		}

		return null;
	}
	
	protected HttpServletRequest checkMultipart(HttpServletRequest req,boolean isHttpServletRequestWrappered) throws MultipartException
    {
		HttpServletRequest request = req;
		if (!(req instanceof HttpServletRequestWrapper) && !(request instanceof MultipartHttpServletRequest) && !isHttpServletRequestWrappered) {
			request = new HttpServletRequestWrapper(req);
			request = (HttpServletRequest) ReflectionUtils.getField(this.requestField,
					request);
			request = new Servlet30RequestWrapper(request);
		}
		
        if (this.multipartResolver != null && this.multipartResolver.isMultipart(request)) { 
            if (!(request instanceof MultipartHttpServletRequest)) {
                return this.multipartResolver.resolveMultipart(request);
            }
        }
        // If not returned before: return original request.
        return request;
    }
	

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}
	
	

}
