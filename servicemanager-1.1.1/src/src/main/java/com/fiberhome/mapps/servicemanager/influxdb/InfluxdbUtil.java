package com.fiberhome.mapps.servicemanager.influxdb;

import java.util.List;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfluxdbUtil {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private InfluxDB influxDB;

	private String dbName;

	public String getdbName() {
		return dbName;
	}

	public InfluxdbUtil(String Url, String db, String user, String pass) {
		influxDB = InfluxDBFactory.connect(Url, user, pass);
		dbName = db;
	}

	public void write() {
		// BatchPoints batchPoints = BatchPoints.database(dbName).tag("async",
		// "true")
		// .consistency(ConsistencyLevel.ALL).build();
		// Point point1 =
		// Point.measurement("cpu").time(System.currentTimeMillis(),
		// TimeUnit.MILLISECONDS)
		// .addField("idle", 90L).addField("user", 9L).addField("system",
		// 1L).build();
		// Point point2 =
		// Point.measurement("disk").time(System.currentTimeMillis(),
		// TimeUnit.MILLISECONDS)
		// .addField("used", 80L).addField("free", 1L).build();
		// batchPoints.point(point1);
		// batchPoints.point(point2);
		// influxDB.write(batchPoints);
	}

	public List<Result> query(String statement) {
		LOGGER.debug("===>influxdb查询语句===" + statement);
		Query query = new Query(statement, dbName);
		QueryResult qr = influxDB.query(query);
		List<Result> measurementList = qr.getResults();
		return measurementList;
	}

	public void close() {
		influxDB.close();
	}
	
	public void excute(final String queryStr){
		Query query = new Query(queryStr, dbName,true);
		influxDB.query(query);
	}
	
	public void createRetentionPolicy(final String rpName, final String duration,
			final String shardDuration, final int replicationFactor, final boolean isDefault) {
		StringBuilder queryBuilder = new StringBuilder("CREATE RETENTION POLICY \"");
		queryBuilder.append(rpName).append("\" ON \"").append(dbName).append("\" DURATION ").append(duration)
				.append(" REPLICATION ").append(replicationFactor);
		if (shardDuration != null && !shardDuration.isEmpty()) {
			queryBuilder.append(" SHARD DURATION ");
			queryBuilder.append(shardDuration);
		}
		if (isDefault) {
			queryBuilder.append(" DEFAULT");
		}
		excute(queryBuilder.toString());
	}
	
	public void dropRetentionPolicy(final String rpName) {
		StringBuilder queryBuilder = new StringBuilder("drop RETENTION POLICY \"");
		queryBuilder.append(rpName).append("\" ON \"").append(dbName).append("\" ");
		excute(queryBuilder.toString());
	}
	
	public void createContinuousQuery(final String cqName, final String sampleSql) {
		StringBuilder queryBuilder = new StringBuilder("CREATE CONTINUOUS QUERY \"");
		queryBuilder.append(cqName).append("\" ON \"").append(dbName).append("\" BEGIN ").append(sampleSql)
				.append(" END ");
		excute(queryBuilder.toString());
	}
	
	public void dropContinuousQuery(final String cqName) {
		StringBuilder queryBuilder = new StringBuilder("drop CONTINUOUS QUERY \"");
		queryBuilder.append(cqName).append("\" ON \"").append(dbName).append("\" ");
		excute(queryBuilder.toString());
	}

}
