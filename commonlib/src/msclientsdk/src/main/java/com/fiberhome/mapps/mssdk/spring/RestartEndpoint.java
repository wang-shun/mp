package com.fiberhome.mapps.mssdk.spring;

import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ReflectionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextStartedEvent;
//import org.springframework.integration.monitor.IntegrationMBeanExporter;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.support.JmxUtils;

/**
 * An endpoint that restarts the application context. Install as a bean and also register
 * a {@link RestartListener} with the {@link SpringApplication} that starts the context.
 * Those two components communicate via an {@link ApplicationEvent} and set up the state
 * needed to restart the context.
 *
 * @author Dave Syer
 *
 */
@ConfigurationProperties("endpoints.restart")
@ManagedResource
public class RestartEndpoint extends AbstractEndpoint<Boolean>
		implements ApplicationListener<ContextStartedEvent>, ApplicationContextAware {

	private static Log logger = LogFactory.getLog(RestartEndpoint.class);

	public RestartEndpoint() {
		super("restart", true, false);
	}

	private ConfigurableApplicationContext context;

	private ContextStartedEvent event;

//	private IntegrationShutdown integrationShutdown;

	private long timeout;

	@ManagedAttribute
	public long getTimeout() {
		return this.timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

//	public void setIntegrationMBeanExporter(Object exporter) {
//		if (exporter != null) {
//			this.integrationShutdown = new IntegrationShutdown(exporter);
//		}
//	}

	@Override
	public void onApplicationEvent(ContextStartedEvent input) {
		this.event = input;
		if (this.context == null) {
			this.context = (ConfigurableApplicationContext)this.event.getApplicationContext();
		}
	}

	@Override
	public Boolean invoke() {
		try {
			restart();
			logger.info("Restarted");
			return true;
		}
		catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.info("Could not restart", e);
			}
			else {
				logger.info("Could not restart: " + e.getMessage());
			}
			return false;
		}
	}

	public void restart() {
		if (this.context != null) {
			this.context.close();
			// If running in a webapp then the context classloader is probably going to
			// die so we need to revert to a safe place before starting again
		}
		
		Thread reloadThread = new Thread() {
			public void run() {
				MBeanServer ms = JmxUtils.locateMBeanServer();
				Set<ObjectInstance> set = ms.queryMBeans(null, null);
				for (ObjectInstance oi : set) {
					if ("org.apache.catalina.mbeans.ContextMBean".equals(oi.getClassName())) {
						try {
							ms.invoke(oi.getObjectName(), "reload", null, null);
						} catch (InstanceNotFoundException | ReflectionException | MBeanException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		};
		reloadThread.start();
	}

	@ManagedAttribute
	public boolean isRunning() {
		if (this.context != null) {
			return this.context.isRunning();
		}
		return false;
	}

	@ManagedOperation
	public synchronized void pause() {
		if (this.context != null) {
			this.context.stop();
		}
	}

	@ManagedOperation
	public synchronized void resume() {
		if (this.context != null) {
			this.context.start();
		}
	}

	private void overrideClassLoaderForRestart() {
//		ClassUtils.overrideThreadContextClassLoader(
//				this.application.getClass().getClassLoader());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = (ConfigurableApplicationContext)applicationContext;
	}

//	private class IntegrationShutdown {
//
//		private IntegrationMBeanExporter exporter;
//
//		public IntegrationShutdown(Object exporter) {
//			this.exporter = (IntegrationMBeanExporter) exporter;
//		}
//
//		public void stop(long timeout) {
//			this.exporter.stopActiveComponents(timeout);
//		}
//	}

}