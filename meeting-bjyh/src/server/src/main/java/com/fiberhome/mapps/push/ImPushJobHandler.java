package com.fiberhome.mapps.push;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fiberhome.mapps.contact.service.MplusAccessService;
import com.fiberhome.mapps.meeting.utils.JsonUtil;
import com.fiberhome.mapps.redismq.Job;
import com.fiberhome.mapps.redismq.JobHandler;

@Component
public class ImPushJobHandler implements JobHandler
{
    @Autowired
    MplusAccessService     accessService;

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public boolean handle(String jobId, Job job)
    {
        LOGGER.debug("开始执行Job推送代办");
        String jsonStr = job.getDetail();
        LOGGER.info(jsonStr);
        Map<String, Object> jobMap = JsonUtil.getMapFromJsonStr(jsonStr);
        String ecid = (String) jobMap.get("ecid");
        String userIds = (String) jobMap.get("userIds");
        String title = (String) jobMap.get("title");
        String content = (String) jobMap.get("content");
        String scheme = (String) jobMap.get("scheme");
        // EventParam param = null;
        // if (jobMap.get("param") != null)
        // {
        // param = (EventParam) jobMap.get("param");
        // }
        try
        {
            accessService.pushReceiveEvent(ecid, userIds, title, content, scheme);
        }
        catch (Exception e)
        {
            LOGGER.error("发送代办事件失败：{}", e);
            return false;
        }
        return true;

    }

}
