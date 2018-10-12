package com.fiberhome.mapps.redismq;

public class ScheduleJob {
	private TaskQueue queue;
	
	private JobHandler handler;
	
	private String jobId;
	
	private Job job;
	
	public ScheduleJob(TaskQueue queue, JobHandler handler, String jobId) {
		this.queue = queue;
		this.handler = handler;
		this.jobId = jobId;
		this.job = queue.getJob(jobId);
	}

	public TaskQueue getQueue() {
		return queue;
	}

	public void setQueue(TaskQueue queue) {
		this.queue = queue;
	}

	public JobHandler getHandler() {
		return handler;
	}

	public void setHandler(JobHandler handler) {
		this.handler = handler;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public Job getJob() {
		return job;
	}
	
	public boolean handle() {
		return this.handler.handle(jobId, job);
	}
	
	public void ack() {
		this.queue.ack(jobId);
	}
	
	public void fail() {
		this.queue.fail(jobId);
	}
}
