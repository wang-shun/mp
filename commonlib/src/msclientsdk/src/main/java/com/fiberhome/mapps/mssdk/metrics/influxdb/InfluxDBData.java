package com.fiberhome.mapps.mssdk.metrics.influxdb;

import java.util.Calendar;
import java.util.Map;

public class InfluxDBData {
	private InfluxDBName name;
	private Long timestamp;
	private Number value;
	private String field;

	protected InfluxDBData() {
		this.name = new InfluxDBName();
	}

	public InfluxDBData(String metric, Number value) {
		this(metric, value, Long.valueOf(System.currentTimeMillis()));
	}

	public InfluxDBData(String metric, Number value, Long timestamp) {
		this(new InfluxDBName(metric), value, timestamp);
	}

	public InfluxDBData(InfluxDBName name, Number value, Long timestamp) {
		this.name = name;
		this.value = value;
		this.timestamp = timestamp;
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		this.getTags().put("y", cal.get(Calendar.YEAR)+"");
		this.getTags().put("m", (cal.get(Calendar.MONTH) + 1) + "");
	}

	public String getMetric() {
		return this.name.getMetric();
	}

	public void setMetric(String metric) {
		this.name.setMetric(metric);
	}

	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Number getValue() {
		return this.value;
	}

	public void setValue(Number value) {
		this.value = value;
	}

	public Map<String, String> getTags() {
		return this.name.getTags();
	}

	public void setTags(Map<String, String> tags) {
		this.name.setTags(tags);
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.name.toString());
		stringBuilder.append(" ");
		stringBuilder.append(this.name.getField()).append("=").append(this.value);
		if ((this.value instanceof Integer) || (this.value instanceof Long) || (this.value instanceof Short))
			stringBuilder.append(".0");

		stringBuilder.append(" ").append(this.timestamp).append("000000");
		return stringBuilder.toString();
	}
}