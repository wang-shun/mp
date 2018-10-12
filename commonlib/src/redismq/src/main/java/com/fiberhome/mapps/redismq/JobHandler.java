package com.fiberhome.mapps.redismq;

public interface JobHandler {
	boolean handle(String jobId, Job job);
}
