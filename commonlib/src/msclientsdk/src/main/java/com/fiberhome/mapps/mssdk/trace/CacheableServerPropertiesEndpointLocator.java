package com.fiberhome.mapps.mssdk.trace;

import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.cloud.sleuth.zipkin.EndpointLocator;
import org.springframework.cloud.sleuth.zipkin.ZipkinProperties;
import org.springframework.context.event.EventListener;
import org.springframework.util.StringUtils;

import zipkin.Endpoint;

/**
 * endpoint缓存，避免ZipkinSpanReportor每次report都要解析
 * @author fh
 *
 */
public class CacheableServerPropertiesEndpointLocator implements EndpointLocator {

	private static final Log log = LogFactory.getLog(MethodHandles.lookup().lookupClass());

	private final ServerProperties serverProperties;
	private final String appName;
	private final InetUtils inetUtils;
	private final ZipkinProperties zipkinProperties;	
	private Integer port;
	
	private InetAddress address = null;

	public CacheableServerPropertiesEndpointLocator(ServerProperties serverProperties,
			String appName, ZipkinProperties zipkinProperties, InetUtils inetUtils) {
		this.serverProperties = serverProperties;
		this.appName = appName;
		this.zipkinProperties = zipkinProperties;
		if (inetUtils == null) {
			this.inetUtils = new InetUtils(new InetUtilsProperties());
		} else {
			this.inetUtils = inetUtils;
		}
	}

	@Override
	public Endpoint local() {
		String serviceName = StringUtils.hasText(this.zipkinProperties.getService().getName()) ?
				this.zipkinProperties.getService().getName() : this.appName;
		if (log.isDebugEnabled()) {
			log.debug("Span will contain serviceName [" + serviceName + "]");
		}
		return Endpoint.builder()
				.serviceName(serviceName)
				.ipv4(getAddress())
				.port(getPort())
				.build();
	}

	@EventListener(EmbeddedServletContainerInitializedEvent.class)
	public void grabPort(EmbeddedServletContainerInitializedEvent event) {
		this.port = event.getEmbeddedServletContainer().getPort();
	}

	private Integer getPort() {
		if (this.port!=null) {
			return this.port;
		}
		Integer port;
		if (this.serverProperties!=null && this.serverProperties.getPort() != null && this.serverProperties.getPort() > 0) {
			port = this.serverProperties.getPort();
		}
		else {
			port = 8080;
		}
		return port;
	}
	
	private InetAddress getInetAddress() {
		if (address == null) {
			if (this.serverProperties != null && this.serverProperties.getAddress() != null) {
				address = this.serverProperties.getAddress();
			}
			else {
				address = this.inetUtils.findFirstNonLoopbackAddress();
			}
		}
		
		return address;
	}

	private int getAddress() {
		return ByteBuffer.wrap(getInetAddress().getAddress()).getInt();
	}

}
