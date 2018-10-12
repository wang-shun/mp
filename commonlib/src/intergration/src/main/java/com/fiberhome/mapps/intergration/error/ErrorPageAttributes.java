package com.fiberhome.mapps.intergration.error;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestAttributes;

public class ErrorPageAttributes extends DefaultErrorAttributes implements ApplicationContextAware{
	ApplicationContext ctx;
	
	@Override
	public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
		Map<String, Object> attr = super.getErrorAttributes(requestAttributes, includeStackTrace);
		String msg = (String)attr.get("message") + "\r\n " + ctx.getApplicationName();
		
		attr.put("message", msg);
		return attr;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}
}
