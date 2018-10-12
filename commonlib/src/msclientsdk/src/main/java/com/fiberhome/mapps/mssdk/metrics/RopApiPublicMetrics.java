package com.fiberhome.mapps.mssdk.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class RopApiPublicMetrics implements PublicMetrics {
	ArrayList<Metric<?>> buffer = new ArrayList<>();

	@Override
	public Collection<Metric<?>> metrics() {
		synchronized (this.buffer) {
			ArrayList<Metric<?>> t = new ArrayList<>(buffer);
			buffer.clear();
			return t;
		}
	}
	
	public void set(Metric<?> metric) {
		synchronized (this.buffer) {
			buffer.add(metric);
		}
	}

}
