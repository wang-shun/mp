package com.fiberhome.mapps.survey.entity;

import java.util.Date;
import java.util.List;

import com.fiberhome.mapps.survey.utils.DateUtil;

public class SuSurveyJson
{

    private String               id;

    private String               title;

    private String               titleHtml;

    private String               status;

    private Date                 effectiveTime;

    private String               effectiveTimeStr;

    private Date                 endTime;

    private String               endTimeStr;

    private String               depUuids;

    private String               userUuids;

    private String               pushTo;

    private String               pager;

    private String               creator;

    private Date                 createTime;

    private String               modifier;

    private Date                 modifiedTime;

    private String               submitTime;

    private Long                 answerPersons;

    private Long                 targetPersons;

    private String               durationStr;

    public List<SuQuestionJson>  suQuestion;

    private List<SuParticipants> suParticipants;

    public String getTitleHtml()
    {
        return titleHtml;
    }

    public void setTitleHtml(String titleHtml)
    {
        this.titleHtml = titleHtml;
    }

    public List<SuParticipants> getSuParticipants()
    {
        return suParticipants;
    }

    public void setSuParticipants(List<SuParticipants> suParticipants)
    {
        this.suParticipants = suParticipants;
    }

    /**
     * @return title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * ��ȡ0 ������ 1 ���� 2 �����
     *
     * @return status - 0 ������ 1 ���� 2 �����
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * ����0 ������ 1 ���� 2 �����
     *
     * @param status
     *            0 ������ 1 ���� 2 �����
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * @return effective_time
     */
    public Date getEffectiveTime()
    {
        return effectiveTime;
    }

    /**
     * @param effectiveTime
     */
    public void setEffectiveTime(Date effectiveTime)
    {
        this.effectiveTime = effectiveTime;
        if (effectiveTime != null)
        {
            this.effectiveTimeStr = DateUtil.sdf().format(effectiveTime);
        }
    }

    /**
     * @return end_time
     */
    public Date getEndTime()
    {
        return endTime;
    }

    /**
     * @param endTime
     */
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
        if (endTime != null)
        {
            this.endTimeStr = DateUtil.sdf().format(endTime);
        }
    }

    public String getDepUuids()
    {
        return depUuids;
    }

    public void setDepUuids(String depUuids)
    {
        this.depUuids = depUuids;
    }

    public String getUserUuids()
    {
        return userUuids;
    }

    public void setUserUuids(String userUuids)
    {
        this.userUuids = userUuids;
    }

    /**
     * @return push_to
     */
    public String getPushTo()
    {
        return pushTo;
    }

    /**
     * @param pushTo
     */
    public void setPushTo(String pushTo)
    {
        this.pushTo = pushTo;
    }

    /**
     * ��ȡ���ŷָ�����ʾ��ҳ���ڵ�N��������棨����ͳ�Ʋ����ҳ��
     *
     * @return pager - ���ŷָ�����ʾ��ҳ���ڵ�N��������棨����ͳ�Ʋ����ҳ��
     */
    public String getPager()
    {
        return pager;
    }

    /**
     * ���ö��ŷָ�����ʾ��ҳ���ڵ�N��������棨����ͳ�Ʋ����ҳ��
     *
     * @param pager
     *            ���ŷָ�����ʾ��ҳ���ڵ�N��������棨����ͳ�Ʋ����ҳ��
     */
    public void setPager(String pager)
    {
        this.pager = pager;
    }

    /**
     * @return creator
     */
    public String getCreator()
    {
        return creator;
    }

    /**
     * @param creator
     */
    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime()
    {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    /**
     * @return modifier
     */
    public String getModifier()
    {
        return modifier;
    }

    /**
     * @param modifier
     */
    public void setModifier(String modifier)
    {
        this.modifier = modifier;
    }

    /**
     * @return modified_time
     */
    public Date getModifiedTime()
    {
        return modifiedTime;
    }

    /**
     * @param modifiedTime
     */
    public void setModifiedTime(Date modifiedTime)
    {
        this.modifiedTime = modifiedTime;
    }

    public List<SuQuestionJson> getSuQuestion()
    {
        return suQuestion;
    }

    public void setSuQuestion(List<SuQuestionJson> suQuestion)
    {
        this.suQuestion = suQuestion;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getSubmitTime()
    {
        return submitTime;
    }

    public void setSubmitTime(String submitTime)
    {
        this.submitTime = submitTime;
    }

    public Long getAnswerPersons()
    {
        return answerPersons;
    }

    public void setAnswerPersons(Long answerPersons)
    {
        this.answerPersons = answerPersons;
    }

    public Long getTargetPersons()
    {
        return targetPersons;
    }

    public void setTargetPersons(Long targetPersons)
    {
        this.targetPersons = targetPersons;
    }

    public String getDurationStr()
    {
        return durationStr;
    }

    public void setDurationStr(String durationStr)
    {
        this.durationStr = durationStr;
    }

    public String getEffectiveTimeStr()
    {
        return effectiveTimeStr;
    }

    public void setEffectiveTimeStr(String effectiveTimeStr)
    {
        this.effectiveTimeStr = effectiveTimeStr;
    }

    public String getEndTimeStr()
    {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr)
    {
        this.endTimeStr = endTimeStr;
    }

}