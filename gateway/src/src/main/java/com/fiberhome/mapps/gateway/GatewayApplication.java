package com.fiberhome.mapps.gateway;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.multipart.MultipartResolver;

import com.fiberhome.mapps.gateway.health.GatewayHealthIndicator;
import com.fiberhome.mapps.gateway.route.DynamicRouteLocator;
import com.fiberhome.mapps.gateway.route.RouteUpdateEvent;
import com.fiberhome.mapps.gateway.security.AppSecretManager;
import com.fiberhome.mapps.gateway.security.filter.AuthFilter;
import com.fiberhome.mapps.gateway.security.filter.CookiePathFilter;
import com.fiberhome.mapps.gateway.trace.ErrorSpanDetachFilter;
import com.fiberhome.mapps.gateway.utils.EurekaClientUtils;
import com.fiberhome.mapps.mssdk.ConfigClientConfiguration;
import com.fiberhome.mapps.mssdk.MssdkClientConfig;
import com.fiberhome.mapps.redismq.pubsub.PubSub;
import com.fiberhome.mapps.redismq.pubsub.PubSubConfig;

@Configuration
@ComponentScan
@EnableAutoConfiguration()
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@Import({PubSubConfig.class, MssdkClientConfig.class})
public class GatewayApplication {
	public static void main(String[] args) {
		ConfigClientConfiguration ccc = new ConfigClientConfiguration("mapps-gateway");		
		new SpringApplicationBuilder(GatewayApplication.class).initializers(ccc).web(true).run(args);
	}

	@Bean
	public ApplicationListener<ApplicationReadyEvent> appReadyListener(PubSub pubsub) {
		return new AppReadyListener(pubsub);
	}
	
	@Bean
	public AuthFilter authFilter(DynamicRouteLocator routeLocator, ZuulProperties properties, ProxyRequestHelper proxyRequestHelper, AppSecretManager asm, MultipartResolver multipartResolver) {
		return new AuthFilter(routeLocator, properties, proxyRequestHelper, asm, multipartResolver);
	}
	
	
	@Bean
	public CookiePathFilter cookiePathFilter(DynamicRouteLocator routeLocator, ZuulProperties properties, ProxyRequestHelper proxyRequestHelper, AppSecretManager asm) {
		return new CookiePathFilter(routeLocator, properties, proxyRequestHelper, asm);
	}
	
	@Bean
	public EurekaClientUtils eurekaClientUtils(DiscoveryClient discovery) {
		return new EurekaClientUtils(discovery);
	}
	
	@Bean
	@Scope("singleton")
	public AppSecretManager appSecretManager(EurekaClientUtils clientUtils, StringRedisTemplate template, PubSub pubsub) {
		return new AppSecretManager(clientUtils, template, pubsub);
	}
	
//	@Bean
//	public HealthIndicator gateHealthIndicator() {
//		return new GatewayHealthIndicator();
//	}
	
	@Bean
	public ErrorSpanDetachFilter errorSpanDetachFilter(Tracer tracer) {
		return new ErrorSpanDetachFilter(tracer);
	}
	
//	@Bean
    public FilterRegistrationBean registerSessionCacheFilter() {
    	FilterRegistrationBean registrationBean=new FilterRegistrationBean();
    	
		registrationBean.setFilter(new HiddenHttpMethodFilter());
		List<String> urlPatterns=new ArrayList<String>();
		urlPatterns.add("/zuul/*"); 
		registrationBean.setUrlPatterns(urlPatterns);
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return registrationBean;
    }
	
	@Bean
    public FilterRegistrationBean registerSessionCacheFilter(ErrorSpanDetachFilter filter) {
    	FilterRegistrationBean registrationBean=new FilterRegistrationBean();
    	
		registrationBean.setFilter(filter);
		List<String> urlPatterns=new ArrayList<String>();
		urlPatterns.add("/*"); 
		registrationBean.setUrlPatterns(urlPatterns);
		return registrationBean;
    }
	
	private class AppReadyListener implements ApplicationListener<ApplicationReadyEvent> {
		public final String ROUTE_UPDATE_CHANNEL = "mapps.servicemanager.route.channel";
		PubSub pubsub;
		
		public AppReadyListener(PubSub pubsub) {
			this.pubsub = pubsub;
		}

		@Override
		public void onApplicationEvent(final ApplicationReadyEvent event) {
//			printNginxConfig();
			event.getApplicationContext().publishEvent(new RouteUpdateEvent(event));
			
			// 注册路由更新监听
			pubsub.subscribe(ROUTE_UPDATE_CHANNEL, new MessageListener() {
				@Override
				public void onMessage(Message message, byte[] pattern) {
					event.getApplicationContext().publishEvent(new RouteUpdateEvent(ROUTE_UPDATE_CHANNEL));
				}
				
			});
		}

	}
	
}
