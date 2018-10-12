package com.fiberhome.mapps.survey.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "su_answer_people")
public class SuAnswerPeople
{
    @Id
    private String id;

    @Column(name = "survey_id")
    private String surveyId;

    @Column(name = "person_name")
    private String personName;

    private String answered;

    /**
     * ������ԱΪ�û�id���ⲿ��ԱΪ�ֻ��
     */
    @Column(name = "person_id")
    private String personId;

    /**
     * 0��δ֪ͨ 1���ѷ��� 2�����ʹ�
     */
    @Column(name = "notice_status")
    private String noticeStatus;

    /**
     * inner���ڲ���Ա outer���ⲿ��Ա
     */
    @Column(name = "person_type")
    private String personType;

    @Column(name = "begin_time")
    private Date   beginTime;

    @Column(name = "submit_time")
    private Date   submitTime;

    /**
     * ��λ����
     */
    private Long   duration;

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
    }
}