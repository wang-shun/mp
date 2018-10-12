package com.fiberhome.mapps.survey.entity;

import java.util.Date;

import com.fiberhome.mapps.survey.utils.DateUtil;

public class SuAnswerPeoplePojo
{
    private String id;

    private String surveyId;

    private String personName;

    private String answered;

    private String personId;

    private String noticeStatus;

    private String personType;

    private Date   beginTime;

    private String beginTimeStr;

    private String submitTimeStr;

    private Date   submitTime;

    private Long   duration;

    private String durationStr;

    /**
     * @return id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return survey_id
     */
    public String getSurveyId()
    {
        return surveyId;
    }

    /**
     * @param surveyId
     */
    public void setSurveyId(String surveyId)
    {
        this.surveyId = surveyId;
    }

    /**
     * @return person_name
     */
    public String getPersonName()
    {
        return personName;
    }

    /**
     * @param personName
     */
    public void setPersonName(String personName)
    {
        this.personName = personName;
    }

    /**
     * @return answered
     */
    public String getAnswered()
    {
        return answered;
    }

    /**
     * @param answered
     */
    public void setAnswered(String answered)
    {
        this.answered = answered;
    }

    /**
     * ��ȡ������ԱΪ�û�id���ⲿ��ԱΪ�ֻ��
     *
     * @return person_id - ������ԱΪ�û�id���ⲿ��ԱΪ�ֻ��
     */
    public String getPersonId()
    {
        return personId;
    }

    /**
     * ����������ԱΪ�û�id���ⲿ��ԱΪ�ֻ��
     *
     * @param personId
     *            ������ԱΪ�û�id���ⲿ��ԱΪ�ֻ��
     */
    public void setPersonId(String personId)
    {
        this.personId = personId;
    }

    /**
     * ��ȡ0��δ֪ͨ 1���ѷ��� 2�����ʹ�
     *
     * @return notice_status - 0��δ֪ͨ 1���ѷ��� 2�����ʹ�
     */
    public String getNoticeStatus()
    {
        return noticeStatus;
    }

    /**
     * ����0��δ֪ͨ 1���ѷ��� 2�����ʹ�
     *
     * @param noticeStatus
     *            0��δ֪ͨ 1���ѷ��� 2�����ʹ�
     */
    public void setNoticeStatus(String noticeStatus)
    {
        this.noticeStatus = noticeStatus;
    }

    /**
     * ��ȡinner���ڲ���Ա outer���ⲿ��Ա
     *
     * @return person_type - inner���ڲ���Ա outer���ⲿ��Ա
     */
    public String getPersonType()
    {
        return personType;
    }

    /**
     * ����inner���ڲ���Ա outer���ⲿ��Ա
     *
     * @param personType
     *            inner���ڲ���Ա outer���ⲿ��Ա
     */
    public void setPersonType(String personType)
    {
        this.personType = personType;
    }

    /**
     * @return begin_time
     */
    public Date getBeginTime()
    {
        return beginTime;
    }

    /**
     * @param beginTime
     */
    public void setBeginTime(Date beginTime)
    {
        this.beginTime = beginTime;
        if (beginTime != null)
        {
            this.beginTimeStr = DateUtil.sdfHMS().format(beginTime);
        }
    }

    /**
     * @return submit_time
     */
    public Date getSubmitTime()
    {
        return submitTime;
    }

    /**
     * @param submitTime
     */
    public void setSubmitTime(Date submitTime)
    {
        this.submitTime = submitTime;
        if (submitTime != null)
        {
            this.submitTimeStr = DateUtil.sdfHMS().format(submitTime);
        }
    }

    public String getSubmitTimeStr()
    {
        return submitTimeStr;
    }

    public void setSubmitTimeStr(String submitTimeStr)
    {
        this.submitTimeStr = submitTimeStr;
    }

    /**
     * ��ȡ��λ����
     *
     * @return duration - ��λ����
     */
    public Long getDuration()
    {
        return duration;
    }

    /**
     * ���õ�λ����
     *
     * @param duration
     *            ��λ����
     */
    public void setDuration(Long duration)
    {
        this.duration = duration;
        if (duration != null)
        {
            this.durationStr = DateUtil.secToTime(duration / 1000);
        }
    }

    public String getDurationStr()
    {
        return durationStr;
    }

    public void setDurationStr(String durationStr)
    {
        this.durationStr = durationStr;
    }

    public String getBeginTimeStr()
    {
        return beginTimeStr;
    }

    public void setBeginTimeStr(String beginTimeStr)
    {
        this.beginTimeStr = beginTimeStr;
    }

}