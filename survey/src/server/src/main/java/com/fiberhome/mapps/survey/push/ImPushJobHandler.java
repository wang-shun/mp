package com.fiberhome.mapps.survey.push;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fiberhome.mapps.contact.service.MplusAccessService;
import com.fiberhome.mapps.redismq.Job;
import com.fiberhome.mapps.redismq.JobHandler;
import com.fiberhome.mapps.survey.dao.SuAnswerPeopleMapper;
import com.fiberhome.mapps.survey.utils.JsonUtil;

@Component
public class ImPushJobHandler implements JobHandler
{
    @Autowired
    MplusAccessService     accessService;
    @Autowired
    SuAnswerPeopleMapper   answerPeopleMapper;

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
        String surveyId = (String) jobMap.get("surveyId");
        try
        {
            accessService.pushReceiveEvent(ecid, userIds, title, content, scheme);
            List<String> result = Arrays.asList(StringUtils.split(userIds, ","));
            for (String userId : result)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("surveyId", surveyId);
                map.put("personId", userId);
                answerPeopleMapper.updateNoticeStatusBySurveyPersonId(map);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("发送代办事件失败：{}", e);
            return false;
        }
        return true;

    }

}
