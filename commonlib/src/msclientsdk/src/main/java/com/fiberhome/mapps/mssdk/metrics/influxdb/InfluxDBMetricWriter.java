package com.fiberhome.mapps.mssdk.metrics.influxdb;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.util.SimpleInMemoryRepository;
import org.springframework.boot.actuate.metrics.util.SimpleInMemoryRepository.Callback;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.fiberhome.mapps.mssdk.metrics.MetricName;
import com.fiberhome.mapps.mssdk.metrics.MetricsWriterContext;

public class InfluxDBMetricWriter implements MetricWriter {
	private static Logger LOG = LoggerFactory.getLogger(InfluxDBMetricWriter.class);
	
	private final SimpleInMemoryRepository<Metric<?>> metrics;
	private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
	private static final int DEFAULT_READ_TIMEOUT = 30000;
	private static final Log logger = LogFactory.getLog(InfluxDBMetricWriter.class);
	private RestOperations restTemplate;
	private String url;
	private String user;
	private String password;
	private int bufferSize;
	private String authHeader;
	private MediaType mediaType;
	private final List<InfluxDBData> buffer;
	private InfluxDBNamingStrategy namingStrategy;

	public InfluxDBMetricWriter() {
		this(DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
	}

	public InfluxDBMetricWriter(int connectTimeout, int readTimeout) {
		this.metrics = new SimpleInMemoryRepository<>();

		this.url = "http://localhost:4242/api/put";

		this.bufferSize = 64;

		this.mediaType = MediaType.TEXT_PLAIN;

		this.buffer = new ArrayList<>(this.bufferSize);

		this.namingStrategy = new DefaultInfluxDBNamingStrategy();

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(connectTimeout);
		requestFactory.setReadTimeout(readTimeout);
		this.restTemplate = new RestTemplate(requestFactory);
	}

	public RestOperations getRestTemplate() {
		return this.restTemplate;
	}

	public void setRestTemplate(RestOperations restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setNamingStrategy(InfluxDBNamingStrategy namingStrategy) {
		this.namingStrategy = namingStrategy;
	}

	public void set(Metric<?> value) {
		InfluxDBData data;
		MetricName mn = MetricsWriterContext.get(value.getName());
		if (mn != null) {
			data = new InfluxDBData(this.namingStrategy.getName(mn.getMeasurement()), value.getValue(),
					Long.valueOf(value.getTimestamp().getTime()));
			data.getTags().putAll(mn.getTags());
		} else {
			data = new InfluxDBData(this.namingStrategy.getName(value.getName()), value.getValue(),
					Long.valueOf(value.getTimestamp().getTime()));
		}
		synchronized (this.buffer) {
			this.buffer.add(data);
			if (this.buffer.size() >= this.bufferSize) {
				flush();
			}
		}
	}

	public void flush() {
		List snapshot = getBufferSnapshot();
		if (snapshot.isEmpty())
			return;

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(new MediaType[] { this.mediaType }));
		headers.setContentType(this.mediaType);
		initAuth(headers);
		StringBuilder stringBuilder = new StringBuilder();
		for (Iterator localIterator = snapshot.iterator(); localIterator.hasNext();) {
			InfluxDBData data = (InfluxDBData) localIterator.next();
			stringBuilder.append(data.toString()).append("\n");
		}
		
		try {
			ResponseEntity response = this.restTemplate.postForEntity(this.url,
					new HttpEntity(stringBuilder.toString(), headers), Map.class, new Object[0]);
			if (!(response.getStatusCode().is2xxSuccessful()))
				logger.warn("Cannot write metrics (discarded " + snapshot.size() + " values): " + response.getBody());
		} catch (RestClientException e) {
			LOG.warn("metrics write error: {}", e.getMessage());
		}
		LOG.debug("writed metric records: {}", snapshot.size());
	}

	private void initAuth(HttpHeaders headers) {
		if (this.authHeader == null) {
			this.authHeader = "";

			if (!(StringUtils.isEmpty(this.user))) {
				String auth = this.user + ":" + this.password;
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("UTF-8")));
				this.authHeader = "Basic " + new String(encodedAuth);
			}
		}

		if (!(StringUtils.isEmpty(this.authHeader)))
			headers.set("Authorization", this.authHeader);
	}

	private List<InfluxDBData> getBufferSnapshot() {
		synchronized (this.buffer) {
			if (this.buffer.isEmpty()) {
				return Collections.emptyList();
			}
			List<InfluxDBData> snapshot = new ArrayList<InfluxDBData>(this.buffer);
			this.buffer.clear();
			return snapshot;
		}
	}

	public void increment(Delta<?> delta) {
		final String metricName = delta.getName();
		final int amount = delta.getValue().intValue();
		final Date timestamp = delta.getTimestamp();
		this.metrics.update(metricName, new Callback<Metric<?>>() {
			@Override
			public Metric<?> modify(Metric<?> current) {
				if (current != null) {
					Metric<? extends Number> metric = current;
					return new Metric<Long>(metricName,
							metric.increment(amount).getValue(), timestamp);
				}
				else {
					return new Metric<Long>(metricName, Long.valueOf(amount), timestamp);
				}
			}
		});
		Metric value = (Metric) this.metrics.findOne(metricName);
		set(value);
	}

	public void reset(String metricName) {
		this.metrics.remove(metricName);
	}
}