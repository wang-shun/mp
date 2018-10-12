package com.fiberhome.mos.core.openapi.rop.client;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

public class SpringContextUtils implements ApplicationContextAware {
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}
	
	public static RestTemplate getRestTemplate() {
		if (applicationContext != null) {
			return applicationContext.getBean(RestTemplate.class);
		}
		return null;
	}

}
