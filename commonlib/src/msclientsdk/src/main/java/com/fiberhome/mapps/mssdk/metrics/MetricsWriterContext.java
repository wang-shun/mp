package com.fiberhome.mapps.mssdk.metrics;

import java.util.concurrent.ConcurrentHashMap;

public class MetricsWriterContext {
	final static String HYSTRIX_METRICS_PREFIX = "gauge.hystrix.HystrixCommand.RibbonCommand";
	
	static ConcurrentHashMap<String, MetricName> caches = new ConcurrentHashMap<String, MetricName>();
	
	public static MetricName get(String mnId) {
		if (mnId == null) return null;
		MetricName metric = caches.get(mnId);
		
		if (metric == null && mnId.startsWith(HYSTRIX_METRICS_PREFIX)) {
			String[] splits = mnId.split("[.]");
			if (splits.length >= 6) {
				String appId = splits[4];
				String measurement = "hystrix." + mnId.substring(HYSTRIX_METRICS_PREFIX.length() + appId.length() + 2);
				
				metric = new MetricName(measurement);
				metric.tag("appId", appId);
				
				set(mnId, metric);
			}
		}
		return metric;
	}
	
	public static void set(String mnId, MetricName mn) {
		assert(mn != null);
		caches.put(mnId, mn);
	}
}
