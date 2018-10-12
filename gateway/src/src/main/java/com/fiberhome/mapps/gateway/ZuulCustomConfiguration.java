package com.fiberhome.mapps.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.ZuulProxyConfiguration;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommandFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.fiberhome.mapps.gateway.route.DynamicRouteLocator;
import com.fiberhome.mapps.gateway.route.RouteCache;
import com.fiberhome.mapps.gateway.route.RouteUpdateEvent;
import com.netflix.zuul.http.ZuulServlet;

@Configuration
@EnableConfigurationProperties({ ZuulProperties.class })
@ConditionalOnClass(ZuulServlet.class)
@Import(ServerPropertiesAutoConfiguration.class)
@AutoConfigureBefore(ZuulProxyConfiguration.class)
public class ZuulCustomConfiguration {
	
	@Autowired
	private ZuulProperties properties;

	@Autowired
	private ServerProperties server;

	@Autowired
	private DiscoveryClient discovery;
		
	@Bean
	public DynamicRouteLocator routeLocator() {
		return new DynamicRouteLocator(this.server.getServletPrefix(), this.properties, this.discovery, routeCache());
	}
	
	@Bean
//	@Scope("sigleton")
	public RouteCache routeCache() {
		return new RouteCache();
	}
	
	@Bean
	public ApplicationListener<ApplicationEvent> routeUpdateListener(RouteLocator routeLocator) {
		return new RouteUpdateListener(routeLocator);
	}
	
	private static class RouteUpdateListener implements ApplicationListener<ApplicationEvent> {
		private RouteLocator routeLocator;
		
		public RouteUpdateListener(RouteLocator routeLocator) {
			this.routeLocator = routeLocator;
		}

		@Override
		public void onApplicationEvent(ApplicationEvent event) {
			if (event instanceof RouteUpdateEvent && routeLocator instanceof DynamicRouteLocator) {
				((DynamicRouteLocator)routeLocator).routeUpdate();
			}
		}
		
		
	}
	
	@Bean
    @Autowired
    public RibbonCommandFactory<?> ribbonCommandFactory(SpringClientFactory clientFactory, ZuulProperties zuulProperties)
    {
        return new HttpClientRibbonCommandFactory(clientFactory, zuulProperties);
    }
	
	

}
