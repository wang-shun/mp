package com.fiberhome.mapps.mssdk.trace;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.autoconfig.TraceAutoConfiguration;
import org.springframework.cloud.sleuth.instrument.web.TraceHandlerInterceptor;
import org.springframework.cloud.sleuth.instrument.web.TraceHttpAutoConfiguration;
import org.springframework.cloud.sleuth.instrument.web.TraceWebAutoConfiguration;
import org.springframework.cloud.sleuth.instrument.web.client.TraceWebClientAutoConfiguration;
import org.springframework.cloud.sleuth.instrument.web.client.feign.TraceFeignClientAutoConfiguration;
import org.springframework.cloud.sleuth.log.SleuthLogAutoConfiguration;
import org.springframework.cloud.sleuth.metric.TraceMetricsAutoConfiguration;
import org.springframework.cloud.sleuth.zipkin.EndpointLocator;
import org.springframework.cloud.sleuth.zipkin.ZipkinAutoConfiguration;
import org.springframework.cloud.sleuth.zipkin.ZipkinProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import com.fiberhome.mapps.mssdk.spring.MappsEurekaClientConfig;

@Configuration
@ConditionalOnMissingBean(SleuthLogAutoConfiguration.class)
@ConditionalOnProperty(value = {"not.spring.boot", "spring.sleuth.enabled"}, matchIfMissing = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@AutoConfigureAfter(MappsEurekaClientConfig.class)
//@ComponentScan(basePackages = {"org.springframework.cloud.sleuth"})
@Import({ZipkinAutoConfiguration.class,
	SleuthLogAutoConfiguration.class, TraceAutoConfiguration.class, 
	TraceHttpAutoConfiguration.class, TraceHttpAutoConfiguration.class, 
	TraceWebAutoConfiguration.class, TraceMetricsAutoConfiguration.class,
	TraceWebClientAutoConfiguration.class, TraceFeignClientAutoConfiguration.class
	})
public class SpringMvcSleuthConfig{
	public SpringMvcSleuthConfig() {
		System.out.println("Sleuth Auto config");
	}
	
	@Autowired BeanFactory beanFactory;

	@Bean
	public TraceHandlerInterceptor traceHandlerInterceptor(BeanFactory beanFactory) {
		return new TraceHandlerInterceptor(beanFactory);
	} 

}
