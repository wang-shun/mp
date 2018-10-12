package com.fiberhome.mapps.servicemanager.request;

import com.rop.AbstractRopRequest;

public class MeasurementSaveRequest extends AbstractRopRequest {
	private String id;
	
	private String name;
	
	private String measurement;
	
	private String retainTime;

	private String retainTimeUnit;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMeasurement() {
		return measurement;
	}

	public void setMeasurement(String measurement) {
		this.measurement = measurement;
	}

	public String getRetainTime() {
		return retainTime;
	}

	public void setRetainTime(String retainTime) {
		this.retainTime = retainTime;
	}

	public String getRetainTimeUnit() {
		return retainTimeUnit;
	}

	public void setRetainTimeUnit(String retainTimeUnit) {
		this.retainTimeUnit = retainTimeUnit;
	}

}
