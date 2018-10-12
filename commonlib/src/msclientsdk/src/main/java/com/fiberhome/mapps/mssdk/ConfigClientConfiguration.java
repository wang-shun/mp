package com.fiberhome.mapps.mssdk;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

import com.fiberhome.mapps.mssdk.exception.GetConfigFailException;

/**
 * 实现ResourceLocator，由Spring进行装载
 * 
 * @author Administrator
 *
 */
public class ConfigClientConfiguration
		implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
	private static final Logger LOG = LoggerFactory.getLogger(ConfigClientConfiguration.class);

	public static final String BOOTSTRAP_PROPERTY_SOURCE_NAME = "bootstrap" + "Properties";

	private static final String[] SECURITY_CHECK_CLASS = new String[] {
			"org.springframework.security.config.http.SessionCreationPolicy",
			"org.springframework.boot.actuate.autoconfigure.ManagementServerProperties.SessionCreationPolicy" };

	private String appId = "mapps-*";

	public ConfigClientConfiguration() {
		this.appId = null;
	}

	public ConfigClientConfiguration(String appId) {
		this.appId = appId;
	}

	private int order = Ordered.HIGHEST_PRECEDENCE + 10;

	@Override
	public int getOrder() {
		return this.order;
	}

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		
		boolean isSpringBoot = (applicationContext instanceof EmbeddedWebApplicationContext);

		CompositePropertySource composite = new CompositePropertySource(BOOTSTRAP_PROPERTY_SOURCE_NAME);
		ConfigurableEnvironment environment = applicationContext.getEnvironment();

		String eurekaClientEnabled = environment.getProperty("eureka.client.enabled");
		MutablePropertySources propertySources = environment.getPropertySources();
		
		if (null == eurekaClientEnabled) {
			// 装载配置 文件
			try {
				PropertySource<?> propertySource = loadYml("classpath:/application.yml");
				InetUtils inetUtils = new InetUtils(new InetUtilsProperties());
				InetAddress address = inetUtils.findFirstNonLoopbackAddress();

				Map<String, String> exConfig = new HashMap<String, String>();
				exConfig.put("server.ipAddress", address.toString());

				MapPropertySource ps = new MapPropertySource("exconfig", exConfig);
				prepandPropertySources(propertySources, ps);
				if (propertySource != null) {
					prepandPropertySources(propertySources, propertySource);
				}

				// reload this property
				eurekaClientEnabled = environment.getProperty("eureka.client.enabled");

				// 注册监听器
				// RestartEndpoint restartEndpoint =
				// applicationContext.getBean(RestartEndpoint.class);
			} catch (IOException e) {
				LOG.warn("装载配置文件application.yml发生错误：{}", e.getMessage(), e);
			}
		}

		if ("true".equals(eurekaClientEnabled)) {
			if (appId == null) {
				appId = environment.getProperty("spring.application.name");
				if (appId == null) {
					LOG.error("请正确配置应用id：spring.application.name");
					return;
				}
			}

			// 从远程服务器获取配置
			String url = environment.getProperty("servicemanager.endpoint");
			if (url == null || url.trim().length() == 0) {
				LOG.error("请配置服务管理中心的地址：servicemanager.endpoint!");
				System.exit(1);
			}
			ServiceClient client = new ServiceClient(url, appId);
			Map<String, String> config = new HashMap<String, String>();
			try {
				config = client.getConfig(true);
			} catch (GetConfigFailException e) {
				LOG.error("获取配置发生错误:{}", e.getMessage(), e);
				throw new RuntimeException(e);
			}

			MapPropertySource ps = new MapPropertySource("applicationConfig: [profile=cloud]", config);

			overridePropertySources(propertySources, ps);
			
			// disable servo metrics
			Map<String, String> custom = new HashMap<String, String>();
			custom.put("spring.metrics.servo.enabled", "false");
			custom.put("endpoints.metrics.filter.enabled", "false");
			custom.put("not.spring.boot", isSpringBoot? "false" : "true");
//			custom.put("spring.sleuth.enabled", "false");
			if (ClassUtils.isPresent(SECURITY_CHECK_CLASS[0], null)
					|| ClassUtils.isPresent(SECURITY_CHECK_CLASS[1], null)) {
				custom.put("management.security.enabled", "false");
			}
			
			MapPropertySource mpr = new MapPropertySource("customConfig", custom);
			prepandPropertySources(propertySources, mpr);
		}

	}

	private PropertySource<?> loadYml(String location) throws IOException {
		PropertySourceLoader propertiesLoader = new YamlPropertySourceLoader();

		Resource resource = new DefaultResourceLoader().getResource(location);
		PropertySource<?> propertySource = null;
		StringBuilder msg = new StringBuilder();
		if (resource != null && resource.exists()) {
			String name = "applicationConfig: [" + location + "]";
			propertySource = propertiesLoader.load(name, resource, null);
			if (propertySource != null) {
				msg.append("Loaded ");
			} else {
				msg.append("Skipped (empty) ");
			}
		} else {
			msg.append("Skipped ");
		}
		msg.append("config file ");
		msg.append("'").append(location).append("'");

		if (resource == null || !resource.exists()) {
			msg.append(" resource not found");
			LOG.trace(msg.toString());
		} else {
			LOG.debug(msg.toString());
		}
		return propertySource;
	}

	private void overridePropertySources(MutablePropertySources propertySources, PropertySource ps) {
		if (propertySources.contains(BOOTSTRAP_PROPERTY_SOURCE_NAME)) {
			propertySources.remove(BOOTSTRAP_PROPERTY_SOURCE_NAME);
		}
		propertySources.addFirst(ps);
		return;
	}

	private void prepandPropertySources(MutablePropertySources propertySources, PropertySource ps) {
		propertySources.addLast(ps);
		return;
	}

}
