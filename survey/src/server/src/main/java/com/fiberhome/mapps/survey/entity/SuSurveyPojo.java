package com.fiberhome.mapps.survey.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fiberhome.mapps.survey.utils.DateUtil;

public class SuSurveyPojo
{
    private String id;

    private String ecid;

    private String title;

    private String status;

    private String answered;

    private Date   effectiveTime;

    private String effectiveTimeStr;

    private Date   endTime;
    private String endTimeStr;

    private Long   targetPersons;

    private Long   answerPersons;

    private String pushTo;

    private String pager;

    private String creator;

    private Date   createTime;

    private String createTimeStr;

    private String surveyCode;

    private String modifier;

    private Date   modifiedTime;

    private String mystatus;

    public String getMystatus()
    {
        return mystatus;
    }

    public void setMystatus(String mystatus)
    {
        this.mystatus = mystatus;
    }

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
     * @return ecid
     */
    public String getEcid()
    {
        return ecid;
    }

    /**
     * @param ecid
     */
    public void setEcid(String ecid)
    {
        this.ecid = ecid;
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
     * @param status 0 ������ 1 ���� 2 �����
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
            this.effectiveTimeStr = DateUtil.sdfHM().format(effectiveTime);
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

    public String getEndTimeStr()
    {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr)
    {
        this.endTimeStr = endTimeStr;
    }

    /**
     * @return target_persons
     */
    public Long getTargetPersons()
    {
        return targetPersons;
    }

    /**
     * @param targetPersons
     */
    public void setTargetPersons(Long targetPersons)
    {
        this.targetPersons = targetPersons;
    }

    /**
     * @return answer_persons
     */
    public Long getAnswerPersons()
    {
        return answerPersons;
    }

    /**
     * @param answerPersons
     */
    public void setAnswerPersons(Long answerPersons)
    {
        this.answerPersons = answerPersons;
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
     * @param pager ���ŷָ�����ʾ��ҳ���ڵ�N��������棨����ͳ�Ʋ����ҳ��
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
        if (createTime != null)
        {
            this.createTimeStr = DateUtil.sdfHM().format(createTime);
            this.surveyCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(createTime);

        }
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

    public String getEffectiveTimeStr()
    {
        return effectiveTimeStr;
    }

    public void setEffectiveTimeStr(String effectiveTimeStr)
    {
        this.effectiveTimeStr = effectiveTimeStr;
    }

    public String getCreateTimeStr()
    {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr)
    {
        this.createTimeStr = createTimeStr;
    }

    public String getSurveyCode()
    {
        return surveyCode;
    }

    public void setSurveyCode(String surveyCode)
    {
        this.surveyCode = surveyCode;
    }

    public String getAnswered()
    {
        return answered;
    }

    public void setAnswered(String answered)
    {
        this.answered = answered;
    }

}