package com.fiberhome.mapps.survey.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "su_survey")
public class SuSurvey
{
    @Id
    private String id;

    private String ecid;

    private String title;

    @Column(name = "title_html")
    private String titleHtml;

    /**
     * 0 ������ 1 ���� 2 �����
     */
    private String status;

    @Column(name = "effective_time")
    private Date   effectiveTime;

    @Column(name = "end_time")
    private Date   endTime;

    @Column(name = "target_persons")
    private Long   targetPersons;

    @Column(name = "answer_persons")
    private Long   answerPersons;

    @Column(name = "push_to")
    private String pushTo;

    /**
     * ���ŷָ�����ʾ��ҳ���ڵ�N��������棨����ͳ�Ʋ����ҳ��
     */
    private String pager;

    private String creator;

    @Column(name = "create_time")
    private Date   createTime;

    private String modifier;

    @Column(name = "modified_time")
    private Date   modifiedTime;

    public String getTitleHtml()
    {
        return titleHtml;
    }

    public void setTitleHtml(String titleHtml)
    {
        this.titleHtml = titleHtml;
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
}