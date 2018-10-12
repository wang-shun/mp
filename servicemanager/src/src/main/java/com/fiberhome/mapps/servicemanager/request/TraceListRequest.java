package com.fiberhome.mapps.servicemanager.request;

public class TraceListRequest extends QueryListRequest {
	private String annotationQuery;
	private String endTs;
	private String tracelimit;
	private String lookback;
	private String minDuration;
	private String serviceName;
	private String sortOrder;
	private String spanName;
	public String getAnnotationQuery() {
		return annotationQuery;
	}
	public void setAnnotationQuery(String annotationQuery) {
		this.annotationQuery = annotationQuery;
	}
	public String getEndTs() {
		return endTs;
	}
	public void setEndTs(String endTs) {
		this.endTs = endTs;
	}
	public String getTracelimit() {
		return tracelimit;
	}
	public void setTracelimit(String tracelimit) {
		this.tracelimit = tracelimit;
	}
	public String getLookback() {
		return lookback;
	}
	public void setLookback(String lookback) {
		this.lookback = lookback;
	}
	public String getMinDuration() {
		return minDuration;
	}
	public void setMinDuration(String minDuration) {
		this.minDuration = minDuration;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getSpanName() {
		return spanName;
	}
	public void setSpanName(String spanName) {
		this.spanName = spanName;
	}
}
