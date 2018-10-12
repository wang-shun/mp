package com.fiberhome.mapps.redismq.pubsub;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

public class PubSub {
	private StringRedisTemplate template;
	private RedisMessageListenerContainer container;
	
	public PubSub(StringRedisTemplate template, RedisMessageListenerContainer container) {
		this.template = template;
		this.container = container;
	}
	
	public void publish(String channel, String msg) {
		template.convertAndSend(channel, msg);
	}
		
	public void subscribe(String channel, MessageListener listener) {
		container.addMessageListener( listener, new ChannelTopic(channel));
	}
    
}
