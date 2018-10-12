package com.fiberhome.mapps.mssdk.metrics.influxdb;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.util.ObjectUtils;

public class DefaultInfluxDBNamingStrategy implements InfluxDBNamingStrategy {
	public static final String DOMAIN_KEY = "domain";
	public static final String PROCESS_KEY = "process";
	private Map<String, String> tags = new LinkedHashMap<>();
	private Map<String, InfluxDBName> cache = new HashMap<>();

	public DefaultInfluxDBNamingStrategy() {
		this.tags.put("domain", "app.metrics");
		this.tags.put("process", ObjectUtils.getIdentityHexString(this));
	}

	public void setTags(Map<String, String> staticTags) {
		this.tags.putAll(staticTags);
	}

	public InfluxDBName getName(String name) {
		if (this.cache.containsKey(name))
			return ((InfluxDBName) this.cache.get(name));

		InfluxDBName value = new InfluxDBName(name);
		value.setTags(this.tags);
		this.cache.put(name, value);
		return value;
	}
}