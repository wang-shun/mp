package com.fiberhome.mapps.redismq;

import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class QueueTaskManager implements Runnable {
	private final static Logger LOG = LoggerFactory.getLogger(QueueTaskManager.class);
	
	private ConcurrentHashMap<String, TaskQueue> queueMap = new ConcurrentHashMap<String, TaskQueue>();
	private ConcurrentHashMap<String, JobHandler> handlerMap = new ConcurrentHashMap<String, JobHandler>();
	
	@Autowired
	private DefaultTaskQueueFactory factory;
	
	ThreadPoolTaskScheduler poolScheduler;
	
	public QueueTaskManager() {
		poolScheduler  = new ThreadPoolTaskScheduler();
		poolScheduler.setPoolSize(10);
		poolScheduler.initialize();
		
		poolScheduler.scheduleAtFixedRate(this, 100);
	}
	
	/**
	 * 注册任务
	 * @param taskId
	 * @param handler
	 * @throws TaskRegisterFailException
	 */
	public void addTask(String taskId, JobHandler handler) throws TaskRegisterFailException {
		addTask(taskId, handler, true);
	}
	
	/**
	 * 注册任务
	 * @param taskId
	 * @param handler
	 * @param parallel
	 * @throws TaskRegisterFailException
	 */
	public void addTask(String taskId, JobHandler handler, boolean parallel) throws TaskRegisterFailException {
		TaskQueue queue = factory.getQueue(taskId, parallel);
		queueMap.put(taskId, queue);
		
		// 判断handler为空，则不添加到处理任务列表
		if (handler != null) {
			handlerMap.put(taskId, handler);
		}
		
		poolScheduler.scheduleAtFixedRate(new JobInpsector(queue), 60000);
	}
	
	public String addJob(String taskId, Job job) throws JobAddException {
		TaskQueue queue = queueMap.get(taskId);
		if (queue == null) {
			LOG.warn("不存在的任务队列：{}", taskId);
			throw new JobAddException("不存在的任务队列：" + taskId);
		}
		
		String jobId = queue.push(job);
		return jobId;
	}
	
	private void execute(TaskQueue queue, JobHandler handler, String jobId) {
		try {
			Job job = queue.getJob(jobId);
			if (job == null) {
				// job 不存在，则进行ack，从任务队列中移除。
				LOG.warn("未从任务列表中获取到id为：{}的任务，数据可能被异常处理", jobId);
				queue.ack(jobId);
				return;
			}
			ScheduleJob scJob = new ScheduleJob(queue, handler, jobId);
			Callable<Boolean> callable = new JobExecutor(scJob);
			Future<Boolean> future = poolScheduler.submit(callable);
			
			poolScheduler.execute(new JobTimeoutWatcher(future, job.getAckTimeout() + 100));
			
			if (future.get()) {
				LOG.debug("任务{}执行成功，确认", jobId);
				scJob.ack();
				
			} else {
				LOG.debug("任务{}执行失败，进入失败队列", jobId);
				scJob.fail();
			}
		} catch(Exception ex) {
			LOG.error("execute error", ex);
			queue.fail(jobId);
		}
	}

	@Override
	public void run() {		
		Enumeration<String> keys = queueMap.keys();
		while (keys.hasMoreElements()) {
			String taskId = keys.nextElement();
			TaskQueue queue = queueMap.get(taskId);
			JobHandler handler = handlerMap.get(taskId);
			
			String jobId;
			int countDown = 100;
			do {
				jobId = queue.pop();
				if (jobId != null) {
					countDown--;
					this.execute(queue, handler, jobId);
				}
			} while (jobId != null && countDown > 0);
			
			try {
				queue.ackTimeout();
				queue.retry();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public boolean isFailJob(String taskId, String jobId) {
		TaskQueue queue = this.queueMap.get(taskId);
		if (queue != null && jobId != null) {
			return queue.isFail(jobId);
		}
		return false;
	}
	
	public void clean(String taskId) {
		TaskQueue queue = this.queueMap.get(taskId);
		queue.clean();
	}
	
	private class JobExecutor implements Callable<Boolean> {
		private ScheduleJob scJob;
		
		public JobExecutor(ScheduleJob scJob) {
			this.scJob = scJob;
		}
		
		@Override
		public Boolean call() throws Exception {
			
			boolean success = false;
			try {
				success = scJob.handle();
			} catch(Exception ex) {
				LOG.warn("error Job, ", ex);
				success = false;
			}
			
			return success;
		}
	}
		
	private class JobTimeoutWatcher implements Runnable {
		private Future future;
		private long timeout;
		
		public JobTimeoutWatcher(Future future, long timeout) {
			this.future = future;
			this.timeout = timeout;
		}

		@Override
		public void run() {
			long begin = System.currentTimeMillis();
			try {
				future.get(timeout, TimeUnit.MILLISECONDS);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				LOG.error("任务超时或者被发生异常", e);
				future.cancel(true);
			}
		}		
	}
	
	private class JobInpsector implements Runnable {
		private TaskQueue queue;

		public JobInpsector(TaskQueue queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			queue.inspect();			
		}
		
	}
	
	
}
