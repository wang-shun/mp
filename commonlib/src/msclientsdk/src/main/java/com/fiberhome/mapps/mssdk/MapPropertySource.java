package com.fiberhome.mapps.mssdk;

import java.util.Map;

import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.util.StringUtils;

/**
 * 支持Map类型的PropertySource
 * @author fh
 *
 */
public class MapPropertySource extends EnumerablePropertySource<Map<String, String>> {

	protected MapPropertySource(String name, Map<String, String> source) {
		super(name, source);
	}

	@Override
	public String[] getPropertyNames() {
		return StringUtils.toStringArray(this.source.keySet());
	}

	@Override
	public Object getProperty(String name) {
		return this.source.get(name);
	}
	
	@Override
	public boolean containsProperty(String name) {
		return this.source.containsKey(name);
	}

}
