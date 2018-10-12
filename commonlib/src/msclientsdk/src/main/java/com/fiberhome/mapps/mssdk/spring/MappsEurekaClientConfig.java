package com.fiberhome.mapps.mssdk.spring;

import static org.springframework.cloud.commons.util.IdUtils.getDefaultInstanceId;

import java.lang.reflect.Constructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.UtilAutoConfiguration;
import org.springframework.cloud.netflix.eureka.CloudEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.eureka.InstanceInfoFactory;
import org.springframework.cloud.netflix.eureka.MutableDiscoveryClientOptionalArgs;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.DiscoveryClient.DiscoveryClientOptionalArgs;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;


@Configuration
@ConditionalOnMissingBean(EurekaClientAutoConfiguration.class)
@ConditionalOnProperty(value = "not.spring.boot", matchIfMissing = true)
@Import(UtilAutoConfiguration.class)
public class MappsEurekaClientConfig {
	@Value("${server.port:${SERVER_PORT:${PORT:8080}}}")
	int nonSecurePort;

	@Value("${server.port:${SERVER_PORT:${PORT:8080}}}")
	int managementPort;

	@Value("${eureka.instance.hostname:${EUREKA_INSTANCE_HOSTNAME:}}")
	String hostname;

	@Autowired
	ConfigurableEnvironment env;

	@Bean
	@ConditionalOnMissingBean(value = EurekaClientConfig.class, search = SearchStrategy.CURRENT)
	public EurekaClientConfigBean eurekaClientConfigBean() {
		EurekaClientConfigBean client = new EurekaClientConfigBean();
		if ("bootstrap".equals(this.env.getProperty("spring.config.name"))) {
			// We don't register during bootstrap by default, but there will be another
			// chance later.
			client.setRegisterWithEureka(false);
		}
		return client;
	}

	@Bean
	@ConditionalOnMissingBean(value = EurekaInstanceConfig.class, search = SearchStrategy.CURRENT)
	public EurekaInstanceConfigBean eurekaInstanceConfigBean(InetUtils inetUtils) {
		EurekaInstanceConfigBean instance = new EurekaInstanceConfigBean(inetUtils);
		instance.setNonSecurePort(this.nonSecurePort);
		instance.setInstanceId(getDefaultInstanceId(this.env));
		if (this.managementPort != this.nonSecurePort && this.managementPort != 0) {
			if (StringUtils.hasText(this.hostname)) {
				instance.setHostname(this.hostname);
			}
			String scheme = instance.getSecurePortEnabled() ? "https" : "http";
			instance.setStatusPageUrl(scheme + "://" + instance.getHostname() + ":"
					+ this.managementPort + instance.getStatusPageUrlPath());
			instance.setHealthCheckUrl(scheme + "://" + instance.getHostname() + ":"
					+ this.managementPort + instance.getHealthCheckUrlPath());
		}
		return instance;
	}
	
	@Bean
	@ConditionalOnMissingBean(value = DiscoveryClientOptionalArgs.class, search = SearchStrategy.CURRENT)
	public MutableDiscoveryClientOptionalArgs discoveryClientOptionalArgs() {
		return new MutableDiscoveryClientOptionalArgs();
	}

	@Bean
	public DiscoveryClient discoveryClient(EurekaInstanceConfig config,
			EurekaClient client) {
		return new EurekaDiscoveryClient(config, client);
	}
	
	@Bean
	@ConditionalOnMissingBean(value = ApplicationInfoManager.class, search = SearchStrategy.CURRENT)
	public ApplicationInfoManager eurekaApplicationInfoManager(
			EurekaInstanceConfig config) {
		InstanceInfo instanceInfo = new InstanceInfoFactory().create(config);
		return new ApplicationInfoManager(config, instanceInfo);
	}
	

	@Bean 
	public EurekaClient eurekaClient(ApplicationInfoManager manager,
			EurekaClientConfig config, DiscoveryClientOptionalArgs args, ApplicationContext context) {
		
		try {
			manager.setInstanceStatus(InstanceStatus.UP);
			Constructor constructor = null;
			
			try {
				constructor = ReflectUtils.getConstructor(CloudEurekaClient.class, new Class[]{
						ApplicationInfoManager.class, EurekaClientConfig.class, 
						DiscoveryClientOptionalArgs.class, ApplicationContext.class});
			} catch (Exception ex) {
				// 不是spring cloud 1.1版本，切换到1.3的初始方式
			}
			
			if (constructor == null) {
				constructor = ReflectUtils.getConstructor(CloudEurekaClient.class, new Class[]{
						ApplicationInfoManager.class, EurekaClientConfig.class, 
						DiscoveryClientOptionalArgs.class, ApplicationEventPublisher.class});
			} 
			if(!"false".equals(env.getProperty("eureka.client.enabled"))){
				if (constructor != null) {
					return (CloudEurekaClient)constructor.newInstance(manager, config, args, context);
				} 
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
}
