package com.fiberhome.mapps.survey.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "su_survey_template")
public class SuSurveyTemplate
{
    @Id
    private String id;

    private String ecid;

    private String title;

    @Column(name = "title_html")
    private String titleHtml;

    /**
     * ����ģ��û�з���
     */
    @Column(name = "group_id")
    private String groupId;

    /**
     * 1 ����ģ�� 2 ����ģ��
     */
    private String type;

    /**
     * ���ǹ���ģ�壬�ǿ�Ϊ��Ӧ�û�id��ģ��
     */
    private String owner;

    /**
     * ���ŷָ�����ʾ��ҳ���ڵ�N��������棨����ͳ�Ʋ����ҳ��
     */
    private String pager;

    private String status;

    @Column(name = "use_times")
    private Long   useTimes;

    private Long   questions;

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

    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getModifier()
    {
        return modifier;
    }

    public void setModifier(String modifier)
    {
        this.modifier = modifier;
    }

    public Date getModifiedTime()
    {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime)
    {
        this.modifiedTime = modifiedTime;
    }

}