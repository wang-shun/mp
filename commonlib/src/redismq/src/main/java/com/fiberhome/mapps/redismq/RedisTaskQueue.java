package com.fiberhome.mapps.redismq;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import com.fiberhome.mapps.redismq.utils.JsonUtil;

/**
 * 通过task_queue、todo_queue、ack_queue、fail_queue、retry_queue来实现消息的发送、确认、失败存储、
 * 重试机制。
 * 
 */
public class RedisTaskQueue implements TaskQueue {
	private final static Logger LOG = LoggerFactory.getLogger(RedisTaskQueue.class);

	private final static String TASK_LIST_KEY = "mapps.redismq.task";
	private final static String RETRAY_WATCH_KEY = "mapps.redismq.retrywatch";
	private final static String ACK_WATCH_KEY = "mapps.redismq.ackwatch";
	private String jobMapKey;
	private String taskQueueKey;
	private String ackQueueKey;
	private String failQueueKey;
	private String retryQueueKey;
	private String uniqueQueueKey;

	private StringRedisTemplate template;
	private String taskId;
	private boolean parallel;
	
	private final static long ONEDAY = 24 * 60 * 60; 
	
	public  static DefaultRedisScript<String> script;
	
	static {
		script = new DefaultRedisScript<String>();
		script.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/checkandset.lua")));
		script.setResultType(String.class);
	}

	public RedisTaskQueue(StringRedisTemplate template) {
		this.template = template;
		template.setEnableTransactionSupport(false);
	}

	@Override
	public void init(String taskId, boolean parallel) {
		this.taskId = taskId;
		this.parallel = parallel;

		// Hash
		jobMapKey = "redismq.job.map." + taskId;
		// List
		taskQueueKey = "redismq.task.queue." + taskId;
		// ZSet
		ackQueueKey = "redismq.ack.queue." + taskId;
		// Hash
		failQueueKey = "redismq.fail.queue." + taskId;
		// ZSet
		retryQueueKey = "redismq.retry.queue." + taskId;
		// Zset  
		uniqueQueueKey = "redismq.unique.queue." + taskId;

		template.opsForSet().add(TASK_LIST_KEY, taskId);
	}

	@Override
	public String push(Job job) {
		boolean isUnique = job.getId() != null;
		String jobId = isUnique? job.getId() : UUID.randomUUID().toString();
		
		boolean locked = lockUnique(jobId);
		if (isUnique && !locked) {
			LOG.debug("任务:{}为只执行一次的任务，已经存在于任务队列中，不再添加", jobId);
			return null;
		}
		
		setJob(jobId, job);

		template.opsForList().rightPush(taskQueueKey, jobId);
		LOG.debug("添加任务:{} 到队列: {}, 配置:{}", jobId, taskId, job.getDetail());

		return jobId;
	}
	
	private boolean lockUnique(String jobId) {
		String lockKey = uniqueQueueKey + ":" + jobId;
		
		String haveLock = template.execute(script, Collections.singletonList(lockKey), String.valueOf(ONEDAY));
		if ("OK".equals(haveLock)) {
			LOG.info("添加只执行一次任务：{}成功，该任务惟一性有效期为1天，请确保任务在一天内执行或者失败。", jobId);
			return true;
		} 
		
		return false;
	}

	@Override
	public String pop() {
		// 非并行处理情况下，确认队列中有待处理任务，不pop任务
		if (!parallel && isAcking()) {
			LOG.debug("非并行处理情况下，确认队列中有待处理任务，不pop任务。");
			return null;
		}
		String jobId = template.opsForList().leftPop(taskQueueKey);

		if (jobId != null) {
			Job job = getJob(jobId);
			if (job == null) {
				LOG.warn("任务对象队列中未找到id为：{}的任务", jobId);
				return null;
			}
			// 添加到ack队列中
			long score = System.currentTimeMillis() + job.getAckTimeout();
			LOG.debug("当前任务 :{}，任务信息：{}, 确认超时时间 {}", jobId, job.getDetail(), job.getAckTimeout());
			template.opsForZSet().add(ackQueueKey, jobId, score);

		}
		return jobId;
	}

	@Override
	public void ack(String jobId) {
		assert (jobId != null);

		Job job = getJob(jobId);
		template.opsForZSet().remove(ackQueueKey, jobId);
		template.opsForHash().delete(jobMapKey, jobId);
	}

	@Override
	public void fail(final String jobId) {
		final Job job = getJob(jobId);
		if (job == null) {
			return;
		}

		LOG.info("任务执行失败, 队列: {}, 任务id: {}, 配置: {}", taskId, jobId, job.getDetail());

		final RedisTaskQueue queue = this;

		boolean ets = false;
		template.setEnableTransactionSupport(true);
		List<Object> result = template.execute(new SessionCallback<List<Object>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<Object> execute(RedisOperations operations) throws DataAccessException {
				operations.watch(ACK_WATCH_KEY);
				operations.multi();
				operations.opsForZSet().remove(ackQueueKey, jobId);
				if (job.getRetry() <= 0) {
					// 不重试，则固定时间间隔放到到任务队列中
					operations.opsForZSet().add(retryQueueKey, jobId,
							System.currentTimeMillis() + job.getRetryInterval());
				} else if (job.retryable()) {
					// 更新任务信息
					String json = JsonUtil.toJson(job);
					operations.opsForHash().put(jobMapKey, jobId, json);
					// 任务迁移到重试队列
					operations.opsForZSet().add(retryQueueKey, jobId, System.currentTimeMillis() + job.nextInterval());
				} else {
					operations.opsForHash().put(failQueueKey, jobId, new Date().getTime() + "");
				}

				return operations.exec();
			}

		});
		template.setEnableTransactionSupport(ets);

	}

	public void ackTimeout() {

		long current = System.currentTimeMillis();

		Set<String> jobIdSet = template.opsForZSet().rangeByScore(ackQueueKey, 0, current);
		if (jobIdSet != null && jobIdSet.size() > 0) {
			LOG.debug("Fetch ack score of 0-{} task", current);
			LOG.info("已经超时任务, 队列: {}, 任务id: {}", taskId, jobIdSet.toString());
		}
		for (String jobId : jobIdSet) {
			fail(jobId);
		}
	}

	@Override
	public Job getJob(String jobId) {
		String detail = (String) template.opsForHash().get(jobMapKey, jobId);
		if (detail == null) {
			return null;
		}
		return (Job) JsonUtil.jsonToObject(detail, Job.class);
	}

	protected void setJob(String jobId, Job job) {
		template.opsForHash().put(jobMapKey, jobId, JsonUtil.toJson(job));
	}

	protected boolean isAcking() {
		return template.opsForZSet().size(ackQueueKey) > 0;
	}

	public void retry() {
		Set<String> jobIdList = template.opsForZSet().rangeByScore(retryQueueKey, 0, System.currentTimeMillis());
		if (jobIdList != null && jobIdList.size() > 0) {
			LOG.info("重试队列中的任务，队列: {}, 任务id: {}", taskId, jobIdList.toString());
		}
		for (final String jobId : jobIdList) {
			boolean ets = false;
			template.setEnableTransactionSupport(true);
			List<Object> result = template.execute(new SessionCallback<List<Object>>() {
				@SuppressWarnings("unchecked")
				@Override
				public List<Object> execute(RedisOperations operations) throws DataAccessException {
					operations.watch(RETRAY_WATCH_KEY);
					operations.multi();
					operations.opsForValue().set(RETRAY_WATCH_KEY, jobId);
					// 从retryQueue 中rem jobId
					operations.opsForZSet().remove(retryQueueKey, jobId);

					// 推送到代办队列中
					operations.opsForList().rightPush(taskQueueKey, jobId);

					return operations.exec();
				}

			});
			template.setEnableTransactionSupport(ets);
		}

		if (LOG.isDebugEnabled() && jobIdList != null && jobIdList.size() > 0) {
			inspect();
		}
	}

	@Override
	public boolean isFail(String jobId) {
		Object result = template.opsForHash().get(failQueueKey, jobId);
		return (null != result);
	}

	@Override
	public boolean isRetry(String jobId) {
		if (jobId == null) {
			return false;
		}
		Object result = template.opsForZSet().score(retryQueueKey, jobId);
		return (null != result);
	}

	public void inspect() {
		// 获取任务列表
		StringBuilder sb = new StringBuilder();

		sb.append("\n================================================================").append("\n");
		sb.append("--队列: [").append(taskId).append("] 信息:").append("\n");

		List<String> taskList = template.opsForList().range(taskQueueKey, 0, -1);
		sb.append("----待执行任务: ").append(taskList.toString()).append("\n");

		Set<String> ackList = template.opsForZSet().range(ackQueueKey, 0, -1);
		sb.append("----待确认任务: ").append(ackList.toString()).append("\n");

		Set<String> retryList = template.opsForZSet().range(retryQueueKey, 0, -1);
		sb.append("----待重试任务: ").append(retryList.toString()).append("\n");

		Set<Object> failList = template.opsForHash().keys(failQueueKey);
		sb.append("----失败的任务: ").append(failList.toString()).append("\n");
		sb.append("================================================================").append("\n");

		LOG.debug(sb.toString());
	}

	public void clean() {
		LOG.info("清除任务：{}队列信息", taskId);
		inspect();
		template.opsForList().trim(taskQueueKey, -1, 0);
		template.opsForZSet().removeRange(ackQueueKey, 0, -1);
		template.opsForZSet().removeRange(retryQueueKey, 0, -1);
		Set<Object> list = template.opsForHash().keys(failQueueKey);
		for (Object o : list) {
			if (o != null) {
				template.opsForHash().delete(failQueueKey, o);
			}
		}

		inspect();
	}

}
