package com.fiberhome.mapps.gateway;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiberhome.mapps.gateway.route.RouteUpdateEvent;
import com.fiberhome.mapps.gateway.security.AppSecretManager;
import com.fiberhome.mapps.gateway.security.token.TokenResponse;

@RestController
public class GatewayController implements ApplicationContextAware{
	private ApplicationContext applicationContext;
	
	private String version = null;
	
    // 通过配置更新的接口    
    @Value("${gateway.security.key}")
    private String securityKey;
    
    @Value("${gateway.token.timeout:7200}")
    private int tokenTimeout;
    
    @Autowired
    AppSecretManager appSecretManager;
    
    @Autowired
	protected ZuulProperties zuulProperties;
    
    @RequestMapping("/rd")
    public String triggerRouteUpdate(String key) {
    	if (!securityKey.equals(key)) {
    		return "ERROR!";
    	}
    	
    	applicationContext.publishEvent(new RouteUpdateEvent(this));
    	return "SUCCESS";
    }
    
    @RequestMapping("/")
    public TokenResponse token(String appkey, String secret) {
    	return appSecretManager.generateAccessToken(appkey, secret, tokenTimeout);
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
    
	@RequestMapping("/checkself")
    public String checkSelf() {
    	return "SUCCESS";
    }
	
	
}
