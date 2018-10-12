package com.fiberhome.mapps.mssdk.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.EndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.EndpointCorsProperties;
import org.springframework.boot.actuate.autoconfigure.EndpointWebMvcManagementContextConfiguration;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.PublicMetricsAutoConfiguration;
import org.springframework.boot.actuate.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.endpoint.EnvironmentEndpoint;
import org.springframework.boot.actuate.endpoint.MetricsEndpoint;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.endpoint.mvc.EndpointHandlerMapping;
import org.springframework.boot.actuate.endpoint.mvc.EnvironmentMvcEndpoint;
import org.springframework.boot.actuate.endpoint.mvc.MetricsMvcEndpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoints;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@ConditionalOnMissingBean(EndpointWebMvcManagementContextConfiguration.class)
@ConditionalOnProperty(value = "not.spring.boot", matchIfMissing = true)
@Import({PublicMetricsAutoConfiguration.class, EndpointAutoConfiguration.class, MetricRepositoryAutoConfiguration.class})
public class EndpointConfig implements ApplicationContextAware, BeanFactoryAware, SmartInitializingSingleton {
	private ApplicationContext applicationContext;

	private BeanFactory beanFactory;
	
	@Autowired(required = false)
	private Collection<PublicMetrics> publicMetrics;
	
	public void afterSingletonsInstantiated() {
		
	}
	
	public static boolean trace(String beanName) {
		System.out.println(beanName);
		return false;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnWebApplication
	public ManagementServerProperties managementServerProperties() {
		return new ManagementServerProperties();
	}
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnWebApplication
	public ServerProperties serverProperties() {
		return new ServerProperties();
	}
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnWebApplication
	public EndpointCorsProperties corsProperties() {
		EndpointCorsProperties endpointCorsProperties = new EndpointCorsProperties();
		endpointCorsProperties.setAllowedMethods(Arrays.asList("GET", "POST"));
		return endpointCorsProperties;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public EndpointHandlerMapping endpointHandlerMapping() {
		ManagementServerProperties managementServerProperties = managementServerProperties();
		Set<? extends MvcEndpoint> endpoints = mvcEndpoints().getEndpoints();
		CorsConfiguration corsConfiguration = getCorsConfiguration(corsProperties());
		EndpointHandlerMapping mapping = new EndpointHandlerMapping(endpoints,
				corsConfiguration);
		boolean disabled = managementServerProperties.getPort() != null
				&& managementServerProperties.getPort() == -1;
		mapping.setDisabled(disabled);
		if (!disabled) {
			mapping.setPrefix(managementServerProperties.getContextPath());
		}
		
		return mapping;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public MvcEndpoints mvcEndpoints() {
		return new MvcEndpoints();
	}
	
	@Bean
	@ConditionalOnBean(EnvironmentEndpoint.class)
	@ConditionalOnEnabledEndpoint("env")
	public EnvironmentMvcEndpoint environmentMvcEndpoint(EnvironmentEndpoint delegate) {
		return new EnvironmentMvcEndpoint(delegate);
	}

	@Bean
	@ConditionalOnBean(MetricsEndpoint.class)
	@ConditionalOnEnabledEndpoint("metrics")
	public MetricsMvcEndpoint metricsMvcEndpoint(MetricsEndpoint delegate) {
		return new MetricsMvcEndpoint(delegate);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public MetricsEndpoint metricsEndpoint() {
		List<PublicMetrics> publicMetrics = new ArrayList<PublicMetrics>();
		if (this.publicMetrics != null) {
			publicMetrics.addAll(this.publicMetrics);
		}
		Collections.sort(publicMetrics, AnnotationAwareOrderComparator.INSTANCE);
		return new MetricsEndpoint(publicMetrics);
	}
	
	@Bean
	@ConditionalOnMissingBean(org.springframework.cloud.context.restart.RestartMvcEndpoint.class)
	public RestartMvcEndpoint restartMvcEndpoint(RestartEndpoint delegate) {
		RestartMvcEndpoint restartMvcEndpoint = new RestartMvcEndpoint(delegate);
		return restartMvcEndpoint;
	}
 	
	@Bean
	@ConditionalOnMissingBean(org.springframework.cloud.context.restart.RestartMvcEndpoint.class)
	public RestartEndpoint restartEndpoint() {
		System.out.println("Restart Endpoint init....");
		return new RestartEndpoint();
	}
	
	
	private CorsConfiguration getCorsConfiguration(EndpointCorsProperties properties) {
		if (CollectionUtils.isEmpty(properties.getAllowedOrigins())) {
			return null;
		}
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(properties.getAllowedOrigins());
		if (!CollectionUtils.isEmpty(properties.getAllowedHeaders())) {
			configuration.setAllowedHeaders(properties.getAllowedHeaders());
		}
		if (!CollectionUtils.isEmpty(properties.getAllowedMethods())) {
			configuration.setAllowedMethods(properties.getAllowedMethods());
		}
		if (!CollectionUtils.isEmpty(properties.getExposedHeaders())) {
			configuration.setExposedHeaders(properties.getExposedHeaders());
		}
		if (properties.getMaxAge() != null) {
			configuration.setMaxAge(properties.getMaxAge());
		}
		if (properties.getAllowCredentials() != null) {
			configuration.setAllowCredentials(properties.getAllowCredentials());
		}
		return configuration;
	}
	
}
