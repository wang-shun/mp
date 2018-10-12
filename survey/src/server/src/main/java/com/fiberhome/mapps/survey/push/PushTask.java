package com.fiberhome.mapps.survey.push;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fiberhome.mapps.contact.pojo.EventParam;
import com.fiberhome.mapps.redismq.Job;
import com.fiberhome.mapps.redismq.JobAddException;
import com.fiberhome.mapps.redismq.QueueTaskManager;
import com.fiberhome.mapps.redismq.TaskRegisterFailException;
import com.fiberhome.mapps.redismq.utils.JsonUtil;

import sun.misc.BASE64Encoder;
import tk.mybatis.mapper.util.StringUtil;

@Component
@Scope("singleton")
public class PushTask
{
    private static final Logger     Logger  = LoggerFactory.getLogger(PushTask.class);

    /**
     * 修改TASK_ID需要慎重，有可能导致历史消息得不到处理
     */
    private final static String     TASK_ID = PushTask.class.getName();

    private static QueueTaskManager taskManager;
    @Autowired
    private Environment env;
    
    @Autowired
    private QueueTaskManager qtm;

    @Autowired
    public void setTaskManager(QueueTaskManager taskManager, ImPushJobHandler handler) throws TaskRegisterFailException
    {
        if(!"default.redis.ip".equals(env.getProperty("spring.redis.host"))){
            PushTask.taskManager = qtm;
            PushTask.taskManager.addTask(TASK_ID, handler);
        }
    }

    // 增加推送任务
    public static void addJob(Job job)
    {
        try
        {
            taskManager.addJob(TASK_ID, job);
        }
        catch (JobAddException e)
        {
            e.printStackTrace();
            Logger.info("增加推送任务失败：{} 原因为： {}", job, e);
        }
    }

    /**
     * 添加推送job
     * 
     * @param ecid
     * @param userIds 发送人
     * @param id 问卷id
     * @param title 问卷标题
     * @param content 推送内容
     * @param param 格式化推送内容 不用传null
     */
    public static void addJobForReceiveEvent(String ecid, String userIds, String id, String title, String content,
            EventParam param)
    {
        Logger.debug("addJobForReceiveEvent:ecid={},userIds={},title={},content={},id={}", ecid, userIds, title,
                content, id);
        try
        {
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            Job job = new Job();
            jsonMap.put("ecid", ecid);
            jsonMap.put("userIds", userIds);
            jsonMap.put("title", title);
            jsonMap.put("content", content);
            String scheme = "";
            if (StringUtil.isNotEmpty(id))
            {
                scheme = "#ID-PageMeetDetail/" + id;
            }
            jsonMap.put("scheme", scheme);
            jsonMap.put("param", param);
            jsonMap.put("surveyId", id);
            String jsonStr = JsonUtil.toJson(jsonMap);
            job.setDetail(jsonStr);
            job.setId(getJobId(jsonStr));
            addJob(job);
            Logger.info("add push job finish");
        }
        catch (Exception e)
        {
            Logger.error("获取jobid异常：{}", e.getMessage());
        }
    }

    @SuppressWarnings("restriction")
    public static String getJobId(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        return base64en.encode(md.digest(str.getBytes("utf-8")));
    }
}
