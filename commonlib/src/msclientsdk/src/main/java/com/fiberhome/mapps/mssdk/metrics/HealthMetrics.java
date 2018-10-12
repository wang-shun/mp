package com.fiberhome.mapps.mssdk.metrics;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.metrics.Metric;

public class HealthMetrics implements PublicMetrics {
	private Collection<HealthIndicator> healthIndicators;
	
	
	public HealthMetrics(Collection<HealthIndicator> healthIndicators) {
		this.healthIndicators = healthIndicators;
	}

	@Override
	public Collection<Metric<?>> metrics() {
		if (healthIndicators == null) {
			return null;
		}
		
		int status = 1;
		Collection<Metric<?>> result = new LinkedHashSet<Metric<?>>();
		
		for (HealthIndicator healthIndicator : healthIndicators) {
			Health health = healthIndicator.health();
			
			if (Status.UNKNOWN.equals(health.getStatus())) {
				continue;
			}
			
			int thisStatus = Status.UP.equals(health.getStatus()) ? 1 : 0;
			if (thisStatus == 0) {
				result.add(new Metric<Integer>("health." + healthIndicator.getClass().getSimpleName(), thisStatus));
			}
			
			status = (status == 1 && thisStatus == 1) ? 1 : 0;
		}
		
		result.add(new Metric<Integer>("health", status));
		
		return result;
	}
	
}
