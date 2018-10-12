package com.fiberhome.mapps.activity;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.stereotype.Component;

@Component
public class UserAgentFakeFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		
		HttpServletRequest wrapper = new HttpServletRequestWrapper(req) {
			@Override
			public String getHeader(String name) {
				if ("User-Agent".equalsIgnoreCase(name)) {
					return "Mozilla/5.0 (Linux; Android 4.4.2; Nexus 4 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.114 Mobile Safari/537.36";
				} else {
					return super.getHeader(name);
				}
			}
		};
		chain.doFilter(wrapper, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
