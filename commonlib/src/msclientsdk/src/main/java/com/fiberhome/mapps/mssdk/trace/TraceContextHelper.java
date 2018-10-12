package com.fiberhome.mapps.mssdk.trace;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

public class TraceContextHelper {
	private final Tracer tracer;
	
	public TraceContextHelper(Tracer tracer) {
		this.tracer = tracer;
	}
	
	public void addTag(String tagName, String tagValue) {
		Span span = tracer.getCurrentSpan();
		if (span != null) {
			span.tag(tagName, tagValue);
		}
	}
}
