package com.fiberhome.mapps.mssdk.spring;

import org.springframework.boot.actuate.autoconfigure.HealthIndicatorAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnMissingBean(HealthIndicatorAutoConfiguration.class)
@Import(HealthIndicatorAutoConfiguration.class)
public class HealthIndicatorConfig {
	
}
