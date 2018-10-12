package com.fiberhome.mapps.survey.entity;

import java.util.List;

public class SuSurveyTemplateJson
{
    private String              id;

    private String              ecid;

    private String              title;

    private String              titleHtml;

    private String              groupId;

    private String              type;

    private String              owner;

    private String              pager;

    private String              status;

    private Long                useTimes;

    private Long                questions;

    private String              submitTime;

    public List<SuQuestionJson> suQuestion;

    public String getTitleHtml()
    {
        return titleHtml;
    }

    public void setTitleHtml(String titleHtml)
    {
        this.titleHtml = titleHtml;
    }

    public String getSubmitTime()
    {
        return submitTime;
    }

    public void setSubmitTime(String submitTime)
    {
        this.submitTime = submitTime;
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
     * ��ȡ����ģ��û�з���
     *
     * @return group_id - ����ģ��û�з���
     */
    public String getGroupId()
    {
        return groupId;
    }

    /**
     * ���ø���ģ��û�з���
     *
     * @param groupId
     *            ����ģ��û�з���
     */
    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    /**
     * ��ȡ1 ����ģ�� 2 ����ģ��
     *
     * @return type - 1 ����ģ�� 2 ����ģ��
     */
    public String getType()
    {
        return type;
    }

    /**
     * ����1 ����ģ�� 2 ����ģ��
     *
     * @param type
     *            1 ����ģ�� 2 ����ģ��
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * ��ȡ���ǹ���ģ�壬�ǿ�Ϊ��Ӧ�û�id��ģ��
     *
     * @return owner - ���ǹ���ģ�壬�ǿ�Ϊ��Ӧ�û�id��ģ��
     */
    public String getOwner()
    {
        return owner;
    }

    /**
     * ���ÿ��ǹ���ģ�壬�ǿ�Ϊ��Ӧ�û�id��ģ��
     *
     * @param owner
     *            ���ǹ���ģ�壬�ǿ�Ϊ��Ӧ�û�id��ģ��
     */
    public void setOwner(String owner)
    {
        this.owner = owner;
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
     * @return status
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * @return use_times
     */
    public Long getUseTimes()
    {
        return useTimes;
    }

    /**
     * @param useTimes
     */
    public void setUseTimes(Long useTimes)
    {
        this.useTimes = useTimes;
    }

    /**
     * @return questions
     */
    public Long getQuestions()
    {
        return questions;
    }

    /**
     * @param questions
     */
    public void setQuestions(Long questions)
    {
        this.questions = questions;
    }

    public List<SuQuestionJson> getSuQuestion()
    {
        return suQuestion;
    }

    public void setSuQuestion(List<SuQuestionJson> suQuestion)
    {
        this.suQuestion = suQuestion;
    }

}