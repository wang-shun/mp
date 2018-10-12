package com.fiberhome.mapps.mssdk.trace.rop;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.web.TraceHandlerInterceptor;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = {"spring.sleuth.enabled"}, matchIfMissing = true)
public class RopTraceAspectConfig {
	
	@Bean
	public RopTraceAspect ropTraceAspect(TraceHandlerInterceptor interceptor, Tracer tracer) {
		return new RopTraceAspect(interceptor, tracer);
	}
}
