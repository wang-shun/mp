package com.fiberhome.mapps.redismq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class DefaultTaskQueueFactory {
	@Autowired
	private StringRedisTemplate template;
	
	public TaskQueue getQueue(String taskId, boolean parallel) {
		TaskQueue queue = new RedisTaskQueue(template);
		queue.init(taskId, parallel);
		return queue;
	}
	
	public void setTemplate(StringRedisTemplate template) {
		this.template = template;
	}
}
