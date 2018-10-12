package com.fiberhome.mapps.mssdk.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.actuate.endpoint.MetricsEndpointMetricReader;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.Assert;

public class PublicMetricsReader extends MetricsEndpointMetricReader {
	private final List<PublicMetrics> publicMetrics;
	
	public PublicMetricsReader(PublicMetrics publicMetrics) {
		this(Collections.singleton(publicMetrics));
	}
	
	public PublicMetricsReader(Collection<PublicMetrics> publicMetrics) {
		super(null);
		Assert.notNull(publicMetrics, "PublicMetrics must not be null");
		this.publicMetrics = new ArrayList<PublicMetrics>(publicMetrics);
		AnnotationAwareOrderComparator.sort(this.publicMetrics);
	}

	@Override
	public Metric<?> findOne(String metricName) {
		for (PublicMetrics metrics : publicMetrics) {
			for (Metric<?> metric : metrics.metrics()) {
				if (metricName.equals(metric.getName())) {
					return metric;
				}
			}
		}
		return null;
	}

	@Override
	public Iterable<Metric<?>> findAll() {
		List<Metric<?>> values = new ArrayList<Metric<?>>((int) count());
		
		for (PublicMetrics metrics : publicMetrics) {
			 Collection<Metric<?>> all = metrics.metrics();
			values.addAll(all);
			
		}
		return values;
	}

	@Override
	public long count() {
		long count = 0;
		for (PublicMetrics metrics : publicMetrics) {
			count += metrics.metrics().size();
		}
		return count;
	}

}
