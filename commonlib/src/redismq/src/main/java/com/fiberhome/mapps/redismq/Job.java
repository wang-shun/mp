package com.fiberhome.mapps.redismq;

import java.io.Serializable;

import com.fiberhome.mapps.redismq.utils.JsonUtil;

public class Job implements Serializable{
	private static final long serialVersionUID = 1014502729598218693L;
	
	/**
	 * jobId，设置则做任务的重复校验
	 */
	private String id;
	
	// 重试次数
	private int retry = 3;
	
	// 重试时间间隔，单位毫秒
	private int retryInterval = 30000;
	
	// 重试步长，n次重试的时间为 retryInterval*retryStep^(n-1) 
	private float retryStep = 2;
	
	// 任务详情
	private String detail;
	
	// 确认的超时时间，单位毫秒，在ack_queue队列中的消息超过超时时间即认为是未正确处理的消息，会重新丢回到到待处理队列
	private int ackTimeout = 60000;
	
	// 已经重试的次数
	private int retryTimes = 0;
	
	// 定时执行表达式
	private String cron;
	
	public Job() {
		this("", 60000);
	}
	
	public Job(String detail, int ackTimeout) {
		this.detail = detail;
		this.ackTimeout = ackTimeout;
	}
	
	public Job(String detail, int ackTimeout, String jobId) {
		this.detail = detail;
		this.ackTimeout = ackTimeout;
		this.id = jobId;
	}
		
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public int getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(int retryInterval) {
		this.retryInterval = retryInterval;
	}

	public float getRetryStep() {
		return retryStep;
	}

	public void setRetryStep(float retryStep) {
		this.retryStep = retryStep;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getAckTimeout() {
		return ackTimeout;
	}

	public void setAckTimeout(int ackTimeout) {
		this.ackTimeout = ackTimeout;
	}

	public int getRetryTimes() {
		return retryTimes;
	}
	
	public boolean retryable() {
		return (retryTimes++ < retry); 
	}
	
	public long nextInterval() {
		if (retryTimes <=0) {
			return 0;
		}
		return (long)(getRetryInterval() * Math.pow(getRetryStep(), (retryTimes - 1)));
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}
	
	public String toString() {
		return JsonUtil.toJson(this);
	}

}
