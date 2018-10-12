package com.fiberhome.mapps.szzj;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.stereotype.Component;

@Component
public class ContentTypeFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if ("mapps.fileservice.file.upload".equals(request.getParameter("method"))) {
			HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper((HttpServletResponse)response) {
				@Override
				public void setContentType(String type) {
					System.out.println(type);
					super.setContentType("text/html; charset=utf-8");
				}
				
				@Override
				public String getContentType() {
					return "text/html; charset=utf-8";
				}
			};
			
			chain.doFilter(request, responseWrapper);
		} else {
			chain.doFilter(request, response);
		}
		
	}

	@Override
	public void destroy() {

	}

}

