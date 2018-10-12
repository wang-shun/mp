package com.fiberhome.mapps.mssdk.metrics.influxdb;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class InfluxDBName {
	private String metric;
	private Map<String, String> tags = new LinkedHashMap<>();
	private String measurement;
	private String field = "value";

	protected InfluxDBName() {
	}

	public InfluxDBName(String metric) {
		this.setMetric(metric);
	}

	private void parse(String metric) {
		int idx = metric.indexOf(".");

		if (idx > 0) {
			measurement = metric.substring(0, idx);
			field = metric.substring(idx + 1);
		} else {
			measurement = metric;
		}

	}

	public String getMetric() {
		return this.metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
		parse(metric);
	}

	public Map<String, String> getTags() {
		return this.tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags.putAll(tags);
	}

	public void tag(String name, String value) {
		this.tags.put(name, value);
	}

	public String getMeasurement() {
		return this.measurement;
	}

	public String getField() {
		return this.field;
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.getMeasurement());
		if (!(getTags().isEmpty()))
			stringBuilder.append(",");

		for (Iterator localIterator = getTags().entrySet().iterator(); localIterator.hasNext();) {
			Map.Entry entry = (Map.Entry) localIterator.next();
			stringBuilder.append((String) entry.getKey()).append("=")
					.append(((String) entry.getValue()).replace(" ", "\\ ")).append(",");
		}
		if (!(getTags().isEmpty())) {
			stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
		}

		return stringBuilder.toString();
	}

	public static void main(String[] args) {
		InfluxDBName name = new InfluxDBName();
		name.setMetric("mem.free");
		System.out.println(name.getField());
		System.out.println(name.getMeasurement());
	}
}