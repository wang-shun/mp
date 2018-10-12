package com.fiberhome.mapps.intergration.security.sso;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import com.rop.config.SystemParameterNames;

@Component
public class SsoFilter implements Filter {
	public final static String SESSION_KEY = "___USER_INFO___";
	public final static String SESSION_ID = "___SESSION_ID___";
	public final static String CLIENT_TYPE_PC = "p";
	public final static String CLIENT_TYPE_WEB = "w";
	public final static String CLIENT_TYPE_CLIENT = "c";
	
	private static Logger LOG = LoggerFactory.getLogger(SsoFilter.class);
	
	@Autowired
	private MultipartResolver multipartResolver;
	
	@Autowired
	private Validator validator;
	
	@Value("${mplus.sso.mngFilter:false}")
	private boolean mngFilter;
	
	public SsoFilter() {
		
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOG.debug("Init SSO Filter (mngFilter: {})", mngFilter);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		
		RequestWrapper requestWrapper = new RequestWrapper(req);
		HttpServletRequest checkRequest = checkMultipart(requestWrapper);
		if (mngFilter) {
			filterM(requestWrapper, checkRequest);
		} else {
			filterP(requestWrapper, checkRequest);
		}
		
		chain.doFilter(checkRequest, response);
	}

	private void filterP(RequestWrapper requestWrapper, HttpServletRequest checkRequest) {
		String sessionId = checkRequest.getParameter(SystemParameterNames.getSessionId());
		String ua = checkRequest.getHeader("User-Agent");		
		String clientType = checkRequest.getParameter("clienttype");
		
		// 判断访问来源
		boolean isMobile = (ua != null && (ua.indexOf("Mobile") >= 0 || ua.indexOf("mplusPcClient") >= 0 || "p".equals(clientType)));
		
		LOG.debug("isMobile: {}, User-Agent: {}", isMobile, ua);
		
		HttpSession httpSession = checkRequest.getSession();
		
		boolean needValidate = false;
		
		// 获取session 
		String orgId = null;
		String ecid = null;
		if (!isMobile) {
			orgId = checkRequest.getParameter("orgUuidDefault");
			ecid = checkRequest.getParameter("orgCodeDefault");
			
			UserInfo user = (UserInfo)httpSession.getAttribute(SESSION_KEY);
			if (user == null || (ecid != null && !ecid.equals(user.getEcid()))) { // session不存在或者ecid不符需要校验
				LOG.debug("session不存在或者ecid不符需要校验");
				needValidate = true;
			}
		}
		
		String oldSessionId = (String)httpSession.getAttribute(SESSION_ID);
		
		if (sessionId != null && !sessionId.equals(oldSessionId)) { // sessionId不符需要校验
			LOG.debug("sessionId:{}和Http Session中保存的{}不符，需要Sso校验", sessionId, oldSessionId);
			needValidate = true;
		}
		
		// 检查是否为新sessionId, 如果是新sessionId则清除http session，同时进行session校验
		if (needValidate) {
			httpSession.removeAttribute(SESSION_KEY);
			httpSession.removeAttribute(SESSION_ID);
			
			UserInfo user = validator.validate(sessionId, isMobile, ecid, orgId);

			httpSession.setAttribute(SESSION_KEY, user);
			httpSession.setAttribute(SESSION_ID, sessionId);
		} else {
			requestWrapper.setSessionId(oldSessionId);
		}
	}
	
	@Override
	public void destroy() {
	}
	
	public MultipartResolver getMultipartResolver() {
		return multipartResolver;
	}

	public void setMultipartResolver(MultipartResolver multipartResolver) {
		this.multipartResolver = multipartResolver;
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	
	protected HttpServletRequest checkMultipart(HttpServletRequest request) throws MultipartException {
		if (this.multipartResolver != null && this.multipartResolver.isMultipart(request)) {
			if (!(request instanceof MultipartHttpServletRequest)) {
				return this.multipartResolver.resolveMultipart(request);
			}
		}
		// If not returned before: return original request.
		return request;
	}
	
	private void filterM(RequestWrapper requestWrapper, HttpServletRequest checkRequest) {
		String sessionId = checkRequest.getParameter(SystemParameterNames.getSessionId());
		
		HttpSession httpSession = checkRequest.getSession();
		
		boolean needValidate = false;
		
		// 获取session 
		String orgId = null;
		String ecid = null;
		orgId = checkRequest.getParameter("orgUuidDefault");
		ecid = checkRequest.getParameter("orgCodeDefault");
		
		UserInfo user = (UserInfo)httpSession.getAttribute(SESSION_KEY);
		if (user == null || (ecid != null && !ecid.equals(user.getEcid()))) { // session不存在或者ecid不符需要校验
			LOG.debug("session不存在或者ecid不符需要校验");
			needValidate = true;
		}
		
		String oldSessionId = (String)httpSession.getAttribute(SESSION_ID);
		
		if (sessionId != null && !sessionId.equals(oldSessionId)) { // sessionId不符需要校验
			LOG.debug("sessionId:{}和Http Session中保存的{}不符，需要Sso校验", sessionId, oldSessionId);
			needValidate = true;
		}
		
		// 检查是否为新sessionId, 如果是新sessionId则清除http session，同时进行session校验
		if (needValidate) {
			httpSession.removeAttribute(SESSION_KEY);
			httpSession.removeAttribute(SESSION_ID);
			
			user = getValidator().validateMng(sessionId, ecid, orgId);

			httpSession.setAttribute(SESSION_KEY, user);
			httpSession.setAttribute(SESSION_ID, sessionId);
		} else {
			requestWrapper.setSessionId(oldSessionId);
		}
	}
}

class RequestWrapper extends HttpServletRequestWrapper {
	private String sessionId;

	public RequestWrapper(HttpServletRequest request) {
		super(request);
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String getParameter(String name) {
		if (SystemParameterNames.getSessionId().equals(name) && this.sessionId != null) {
			return this.sessionId;
		} else  {
			return super.getParameter(name);
		}
	}
}
