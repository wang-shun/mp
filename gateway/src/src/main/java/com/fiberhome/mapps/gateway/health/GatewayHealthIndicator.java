package com.fiberhome.mapps.gateway.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * 健康状态的检查示例程序，用于检测自身的健康状态，应用原本的datasource等资源的健康状态已有采集器
 * @author fh
 *
 */
public class GatewayHealthIndicator implements HealthIndicator, ApplicationContextAware {
    private static Logger LOG = LoggerFactory.getLogger(GatewayHealthIndicator.class);
    String checkUrl = "http://localhost:8762/checkself";
    private Health cache = null;
    private long lastHealthRenewTime = 0;
    
    RestTemplate restTemplate = new RestTemplate();

    @Override
    public Health health() {
        Health health = null;
        long current = System.currentTimeMillis();
        
        // 缓存一分钟，超过一分钟进行自检
        if (cache == null || current - lastHealthRenewTime > 60000 || cache.getStatus() == Status.DOWN) {
            
            try {
                LOG.debug("Custom HealthIndicator working...");
                String result = restTemplate.postForObject(checkUrl, null, String.class);
                lastHealthRenewTime = current;
                
                if ("SUCCESS".equals(result)) {
                    health = Health.up().build();
                }
            } catch (RestClientException e) {
//                LOG.error("Health check error", e);
            }
            if (health == null) {
                health = Health.down().build();
            }
            cache = health;            
        }
        
        return cache;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String port = applicationContext.getEnvironment().getProperty("server.port");
        checkUrl = "http://localhost:" + (port == null ? "8762" : port) + "/checkself";
    }

}
