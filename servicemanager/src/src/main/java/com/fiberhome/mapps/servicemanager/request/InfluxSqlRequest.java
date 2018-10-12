package com.fiberhome.mapps.servicemanager.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class InfluxSqlRequest extends AbstractRopRequest {
	@NotNull
	private String sql;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

}
