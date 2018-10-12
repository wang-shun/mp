package com.fiberhome.mapps.redismq.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class PubSubConfig {
	@Autowired
	private StringRedisTemplate template;

    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory( template.getConnectionFactory());

        return container;
    }
    
    @Bean
    PubSub pubsub() {
    	return new PubSub(template, redisContainer());
    }

    
}
