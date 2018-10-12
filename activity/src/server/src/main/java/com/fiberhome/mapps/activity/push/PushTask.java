package com.fiberhome.mapps.activity.push;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fiberhome.mapps.activity.request.AddActivityRequest;
import com.fiberhome.mapps.redismq.Job;
import com.fiberhome.mapps.redismq.JobAddException;
import com.fiberhome.mapps.redismq.QueueTaskManager;
import com.fiberhome.mapps.redismq.TaskRegisterFailException;
import com.fiberhome.mapps.redismq.utils.JsonUtil;
import com.fiberhome.mapps.utils.LogUtil;

@Component
@Scope("singleton")
public class PushTask {
	private static final Logger Logger = LoggerFactory.getLogger(PushTask.class);

	@Autowired
	private Environment env;
	
	@Autowired
	private QueueTaskManager qtm;
	
	/**
	 * 修改TASK_ID需要慎重，有可能导致历史消息得不到处理
	 */
	private final static String TASK_ID = PushTask.class.getName();

	private static QueueTaskManager taskManager;

	@Autowired
	public void setTaskManager(ImPushJobHandler handler)
			throws TaskRegisterFailException {
		System.out.println("=================================>"+env.getProperty("spring.redis.host"));
		if(!"default.redis.ip".equals(env.getProperty("spring.redis.host"))){
			PushTask.taskManager = qtm;
			PushTask.taskManager.addTask(TASK_ID, handler);
		}
	}

	// 增加推送任务
	public static void addJob(Job job) {
		try {
			taskManager.addJob(TASK_ID, job);
		} catch (JobAddException e) {
			e.printStackTrace();
			Logger.info("增加推送任务失败：{} 原因为： {}", job, e);
		}
	}

	public static void addJobForReceiveEvent(String ecid, String orgId, String createName, String actId,
			AddActivityRequest addReq) {
		Logger.info("add push job,ecid:{},orgId:{},createName:{},actId:{},addreq:{}", ecid, orgId, createName, actId,
				LogUtil.getObjectInfo(addReq));
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Job job = new Job();
		jsonMap.put("ecid", ecid);
		jsonMap.put("title", addReq.getActTitle());
		jsonMap.put("conTel", addReq.getConTel());
		jsonMap.put("address", addReq.getAddress());
		jsonMap.put("startTime", addReq.getActStartTime());
		jsonMap.put("endTime", addReq.getActEndTime());
		jsonMap.put("orgId", orgId);
		jsonMap.put("actId", actId);
		jsonMap.put("enterEndTime", addReq.getEnterEndTime());
		jsonMap.put("createName", createName);
		jsonMap.put("content", addReq.getContent());
		jsonMap.put("priJson", addReq.getPrivilegesJson());
		String jsonStr = JsonUtil.toJson(jsonMap);
		job.setDetail(jsonStr);
		addJob(job);
		Logger.info("add push job finish");
	}

}
