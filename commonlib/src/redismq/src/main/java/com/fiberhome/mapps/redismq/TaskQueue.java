package com.fiberhome.mapps.redismq;

public interface TaskQueue {
	/**
	 * 初始化队列
	 * @param taskId 任务队列id
	 * @param parallel 是否并行处理
	 */
	void init(String taskId, boolean parallel);
	
	/**
	 * push job
	 * @param job
	 */
	String push(Job job);
	
	/**
	 * 弹出任务
	 * @return
	 */
	String pop();
	
   /**
	 * 获取任务详情
	 * @param jobId
	 * @return
	 */
	Job getJob(String jobId);
	
	/**
	 * 确认任务
	 * @param jobId
	 */
	void ack(String jobId);
	
	/**
	 * 确认任务失败
	 * @param jobId
	 */
	void fail(String jobId);
	
	/**
	 * 处理超时任务
	 */
	void ackTimeout();
	
	/**
	 * 重试任务，将超时确认的任务推送到任务队列
	 */
	void retry();
	
	/**
	 * 任务是否失败
	 * @param jobId
	 * @return
	 */
	boolean isFail(String jobId);
	
	/**
	 * 任务是否在重试队列
	 * @param jobId
	 * @return
	 */
	boolean isRetry(String jobId);
	
	/**
	 * 输出队列信息 
	 */
	void inspect();
	
	/**
	 * 清除任务的所有的队列信息
	 */
	void clean();
}
