package com.fiberhome.mapps.mssdk.metrics;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;

public class CustomMetricsWriter {
	CounterService counterService;
	GaugeService guageService;
	
	ConcurrentHashMap<String, String> gaugeCaches = new ConcurrentHashMap<>();
	ConcurrentHashMap<String, String> counterCaches = new ConcurrentHashMap<>();
	
	public CustomMetricsWriter(CounterService counterService, GaugeService guageService) {
		this.counterService = counterService;
		this.guageService = guageService;
	}

	/**
	 * 提交指标数据
	 * @param metric 指标信息，如内存大小
	 * @param value 指标值
	 */
	public void submit(MetricName metric, Double value) {
		String id = getGaugeId(metric.getId());
		MetricsWriterContext.set(id, metric);
		
		guageService.submit(id, value);
	}
	
	/**
	 * 计数器增加
	 * @param metric 计数器指标，如调用次数
	 */
	public void increment(MetricName metric) {
		String id = getCounterId(metric.getId());
		MetricsWriterContext.set(id, metric);
		counterService.increment(id);
	}
	
	/**
	 * 重置计算器
	 * @param metric 计数器指标
	 */
	public void reset(MetricName metric) {
		String id = getCounterId(metric.getId());
		MetricsWriterContext.set(id, metric);
		counterService.reset(id);
	}
	
	private String getGaugeId(String id) {
		String id2 = gaugeCaches.get(id);
		if (id2 == null) {
			id2 = "gauge." + id;
			gaugeCaches.put(id, id2);
		}
		return id2;
	}
	
	private String getCounterId(String id) {
		String id2 = counterCaches.get(id);
		if (id2 == null) {
			id2 = "counter." + id;
			counterCaches.put(id, id2);
		}
		return id2;
	}
}
