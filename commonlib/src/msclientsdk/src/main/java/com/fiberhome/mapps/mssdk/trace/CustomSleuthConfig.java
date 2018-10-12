package com.fiberhome.mapps.mssdk.trace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.zipkin.EndpointLocator;
import org.springframework.cloud.sleuth.zipkin.ZipkinProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义配置
 * @author fh
 *
 */
@Configuration
public class CustomSleuthConfig {
	@Configuration
	@ConditionalOnProperty(value = "spring.zipkin.locator.discovery.enabled", havingValue = "false", matchIfMissing = true)
	protected static class DefaultEndpointLocatorConfiguration {

		@Autowired(required=false)
		private ServerProperties serverProperties;

		@Autowired
		private ZipkinProperties zipkinProperties;

		@Autowired(required=false)
		private InetUtils inetUtils;

		@Value("${spring.application.name:unknown}")
		private String appName;

		@Bean
		public EndpointLocator zipkinEndpointLocator() {
			return new CacheableServerPropertiesEndpointLocator(this.serverProperties, this.appName,
					this.zipkinProperties, this.inetUtils);
		}
	}
	
	@Bean
	@ConditionalOnMissingBean
	public TraceContextHelper traceContextHelper(Tracer tracer) {
		return new TraceContextHelper(tracer);
	}
}
