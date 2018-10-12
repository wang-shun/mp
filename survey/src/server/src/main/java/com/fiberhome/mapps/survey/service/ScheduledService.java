package com.fiberhome.mapps.survey.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import com.fiberhome.mapps.survey.dao.SuAnswerPeopleMapper;
import com.fiberhome.mapps.survey.dao.SuSurveyMapper;
import com.fiberhome.mapps.survey.entity.SuAnswerPeople;
import com.fiberhome.mapps.survey.entity.SuSurveyPojo;
import com.fiberhome.mapps.survey.push.PushTask;
import com.fiberhome.mapps.survey.utils.DateUtil;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean(version = "1.0")
public class ScheduledService
{

    protected final Logger logger   = LoggerFactory.getLogger(getClass());

    @Autowired
    SuSurveyMapper         surveyMapper;
    @Autowired
    ThirdPartAccessService thirdPartAccessService;
    @Autowired
    SuAnswerPeopleMapper   answerPeopleMapper;
    @Value("${survey.scheduled.on-off}")
    Boolean                scheduledOnOff;
    @Value("${survey.scheduled.hour}")
    Integer                scheduledPushHour;

    public final int       PAGE_NUM = 2;

    @Scheduled(cron = "0 1 0 * * *")
    public void closeSurvey()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("current", new Date());
        logger.info("定时关闭截止日期的问卷！");
        surveyMapper.closeSurvey(map);
        logger.info("定时发布问卷！");
        surveyMapper.publishSurvey(map);
    }

    /**
     * 每天早上7点隔10分钟执行一次
     */
    @Scheduled(cron = "1 0 0/1 * * ?")
    public void publishSurvey()
    {
        if (!scheduledOnOff)
        {
            logger.info("定时推送问卷功能已关闭");
        }
        int pushHour = 7;
        if (scheduledPushHour != null)
        {
            pushHour = scheduledPushHour;
        }
        int nowHour = Integer.valueOf(new SimpleDateFormat("H").format(new Date()));
        if (pushHour == nowHour)
        {
            logger.info("定时推送问卷");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("nowDate", DateUtil.sdf().format(new Date()));
            map.put("status", SurveyService.PUBLISH);
            List<SuSurveyPojo> list = surveyMapper.getSurveyForWeb(map);
            for (SuSurveyPojo entity : list)
            {
                pushBySurvey(entity);
            }
        }
        else
        {
            logger.info("非定时推送时间点");
        }
    }

    /**
     * 根据问卷信息进行推送
     * 
     * @param entity ecid,id,title,endTime必须赋值
     */
    public void pushBySurvey(SuSurveyPojo entity)
    {
        String content = "截止日期：不限";
        if (entity.getEndTime() != null)
            content = "截止日期：" + DateUtil.sdf().format(entity.getEndTime());
        SuAnswerPeople record = new SuAnswerPeople();
        record.setSurveyId(entity.getId());
        record.setNoticeStatus("0");
        List<SuAnswerPeople> apList = answerPeopleMapper.select(record);
        for (String userIds : getUserIdsInPage(apList))
        {
            PushTask.addJobForReceiveEvent(entity.getEcid(), userIds, entity.getId(), entity.getTitle(), content, null);
        }
    }

    /**
     * 对答卷人进行分批
     * 
     * @param apList
     * @return
     */
    private List<String> getUserIdsInPage(List<SuAnswerPeople> apList)
    {
        List<String> resultList = new ArrayList<String>();
        List<String> pageList = new ArrayList<String>();
        for (SuAnswerPeople info : apList)
        {
            if (pageList.size() == PAGE_NUM)
            {
                resultList.add(StringUtils.join(pageList, ","));
                pageList.clear();
            }
            pageList.add(info.getPersonId());
        }
        if (pageList.size() > 0)
        {
            resultList.add(StringUtils.join(pageList, ","));
        }
        return resultList;
    }

}
