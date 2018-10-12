package com.fiberhome.mapps.servicemanager.request;

import com.rop.AbstractRopRequest;

public class SampleSaveRequest extends AbstractRopRequest {
	private String id;
	
	private String measurement;
	
	private String sampleSql;
	
	private String remarks;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMeasurement() {
		return measurement;
	}

	public void setMeasurement(String measurement) {
		this.measurement = measurement;
	}

	public String getSampleSql() {
		return sampleSql;
	}

	public void setSampleSql(String sampleSql) {
		this.sampleSql = sampleSql;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
