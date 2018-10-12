package com.fiberhome.mapps.servicemanager.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

import com.fiberhome.mapps.servicemanager.service.RouteManagerService;

public class EurekaInstanceRegisterListener
		implements ApplicationListener<EurekaInstanceRegisteredEvent>, ApplicationContextAware {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private ApplicationContext applicationContext;

	@Override
	public void onApplicationEvent(EurekaInstanceRegisteredEvent event) {
		LOGGER.info("===============>" + event.getInstanceInfo().getAppName() + " registed");

		// 通过applicationContext获取相应的service对象进行操作
		RouteManagerService routeManagerService = (RouteManagerService) applicationContext
				.getBean("routeManagerService");
		// do something
		routeManagerService.regRouteIfNotExists(event.getInstanceInfo());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
