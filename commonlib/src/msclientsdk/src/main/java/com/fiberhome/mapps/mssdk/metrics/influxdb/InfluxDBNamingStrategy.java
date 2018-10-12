package com.fiberhome.mapps.mssdk.metrics.influxdb;

public abstract interface InfluxDBNamingStrategy {
	public abstract InfluxDBName getName(String paramString);
}