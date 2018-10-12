package com.fiberhome.mapps.intergration.session;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartResolver;

import com.fiberhome.mapps.intergration.security.sso.SsoFilter;
import com.fiberhome.mapps.intergration.security.sso.UserInfo;
import com.rop.config.SystemParameterNames;

/**
 * @Author wangwenquan 2012-12-4
 */
@Component
public class RopSessionFilter extends AbstractFilter {
	private static Logger LOG = LoggerFactory.getLogger(RopSessionFilter.class);
	
	@Autowired
	private MultipartResolver multipartResolver;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
	
	@Override
	public void destroy() {
	}

	@Override
	void filterIt(HttpServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String sessionId = request.getParameter(SystemParameterNames.getSessionId());
		
		HttpSession httpSession = request.getSession();
		UserInfo user = (UserInfo)httpSession.getAttribute(SsoFilter.SESSION_KEY);
		
		LOG.debug("RopSessionFilter working, sessionId:{}, User In Session: {}", sessionId, user);
					
		// 设置ROP session
		if (user != null) { 
			RopSession session = new RopSession();
			SessionContext.set(session);
			session.setSessionId(sessionId);
			session.setUser(user);
		}
		
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SessionContext.clear();
		
	}

	
}
