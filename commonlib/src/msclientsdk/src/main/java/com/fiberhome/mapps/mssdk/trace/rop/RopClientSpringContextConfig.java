package com.fiberhome.mapps.mssdk.trace.rop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fiberhome.mos.core.openapi.rop.client.SpringContextUtils;

@Configuration
@ConditionalOnClass(SpringContextUtils.class)
public class RopClientSpringContextConfig {
	@Autowired(required = false)
	SpringContextUtils ropClientSpringContextUtils;
	
	@Bean
	SpringContextUtils ropClientSpringContextUtils() {
		return new SpringContextUtils();
	}
	
	@Bean
	@ConditionalOnMissingBean(RestTemplate.class) 
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
