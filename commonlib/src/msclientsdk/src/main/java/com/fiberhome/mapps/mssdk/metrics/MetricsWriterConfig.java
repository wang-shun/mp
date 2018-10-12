package com.fiberhome.mapps.mssdk.metrics;

import java.util.Collection;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.autoconfigure.MetricExportAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;

import com.fiberhome.mapps.mssdk.metrics.influxdb.DefaultInfluxDBNamingStrategy;
import com.fiberhome.mapps.mssdk.metrics.influxdb.InfluxDBMetricWriter;

@Configuration
@Import({ MetricExportAutoConfiguration.class, MetricRepositoryAutoConfiguration.class, MetricFilterAutoConfiguration.class })
@AutoConfigureAfter(MetricRepositoryAutoConfiguration.class)
public class MetricsWriterConfig {

	@Value("${metrics.influx.url:}")
	String putUrl;

	@Value("${metrics.influx.user:}")
	String user;

	@Value("${metrics.influx.password:}")
	String password;

	@Value("${server.port:-1}")
	String port;

	@Value("${server.ipAddress:${spring.cloud.client.ipAddress:0.0.0.0}}")
	String ip;

	@Value("${spring.application.name:not app name}")
	String appId;
	
	@Bean
	@ExportMetricWriter
	@Scope("singleton")
	@ConditionalOnBean({ CounterService.class, GaugeService.class })
	@ConditionalOnClass({ OncePerRequestFilter.class, HandlerMapping.class })
	@ConditionalOnProperty(name = { "metrics.influx.url" })
	public InfluxDBMetricWriter influxdbGauageWriter() {
		InfluxDBMetricWriter writer = new InfluxDBMetricWriter();
		writer.setUrl(this.putUrl);
		writer.setUser(this.user);
		writer.setPassword(this.password);
		DefaultInfluxDBNamingStrategy namingStrategy = new DefaultInfluxDBNamingStrategy();
		HashMap<String, String> tags = new HashMap<>();

		tags.put("appId", this.appId);
		tags.put("host", this.ip);
		tags.put("port", this.port);
		namingStrategy.setTags(tags);
		writer.setNamingStrategy(namingStrategy);
		return writer;
	}
	
	@Bean
	@ConditionalOnBean({ CounterService.class, GaugeService.class })
	public CustomMetricsWriter customMetricsWriter(CounterService counterService, GaugeService guageService) {
		CustomMetricsWriter customWriter = new CustomMetricsWriter(counterService, guageService);
		return customWriter;
	}
	
//	@Bean
//	public MetricsEndpointMetricReader metricsEndpointMetricReader(MetricsEndpoint metricsEndpoint) {
//		return new MetricsEndpointMetricReader(metricsEndpoint);
//	}
	
	@Bean
	public PublicMetricsReader publicMetricsReader(Collection<PublicMetrics> publicMetrics) {
		return new PublicMetricsReader(publicMetrics);
	}
	
	@Bean
	@ConditionalOnBean(HealthIndicator.class)
	public HealthMetrics healthMetrics(Collection<HealthIndicator> healthIndicators) {
		return new HealthMetrics(healthIndicators);
	}
	
	@Bean
	public MappsMetricsFilter metricsFilter(CustomMetricsWriter writer) {
		return new MappsMetricsFilter(writer);
	}
	
	
}