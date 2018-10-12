package com.fiberhome.mapps.survey.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "su_question")
public class SuQuestion
{
    @Id
    private String id;

    private String ecid;

    private String code;

    private String question;
    private String questionHtml;

    /**
     * 1 ��ѡ 2 ��ѡ 3 ���� 4 �ı� 5 �����ı�
     */
    private String type;

    @Column(name = "sel_max")
    private Long   selMax;

    /**
     * ȱʡΪ1
     */
    @Column(name = "sel_min")
    private Long   selMin;

    private String required;

    /**
     * 1 ���� 0 ͣ��
     */
    private String usable;

    private String creator;

    @Column(name = "create_time")
    private Date   createTime;

    private String modifier;

    @Column(name = "modified_time")
    private Date   modifiedTime;

    public String getQuestionHtml()
    {
        return questionHtml;
    }

    public void setQuestionHtml(String questionHtml)
    {
        this.questionHtml = questionHtml;
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
     * @return code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     * @return question
     */
    public String getQuestion()
    {
        return question;
    }

    /**
     * @param question
     */
    public void setQuestion(String question)
    {
        this.question = question;
    }

    /**
     * ��ȡ1 ��ѡ 2 ��ѡ 3 ���� 4 �ı� 5 �����ı�
     *
     * @return type - 1 ��ѡ 2 ��ѡ 3 ���� 4 �ı� 5 �����ı�
     */
    public String getType()
    {
        return type;
    }

    /**
     * ����1 ��ѡ 2 ��ѡ 3 ���� 4 �ı� 5 �����ı�
     *
     * @param type
     *            1 ��ѡ 2 ��ѡ 3 ���� 4 �ı� 5 �����ı�
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * @return sel_max
     */
    public Long getSelMax()
    {
        return selMax;
    }

    /**
     * @param selMax
     */
    public void setSelMax(Long selMax)
    {
        this.selMax = selMax;
    }

    /**
     * ��ȡȱʡΪ1
     *
     * @return sel_min - ȱʡΪ1
     */
    public Long getSelMin()
    {
        return selMin;
    }

    /**
     * ����ȱʡΪ1
     *
     * @param selMin
     *            ȱʡΪ1
     */
    public void setSelMin(Long selMin)
    {
        this.selMin = selMin;
    }

    /**
     * @return required
     */
    public String getRequired()
    {
        return required;
    }

    /**
     * @param required
     */
    public void setRequired(String required)
    {
        this.required = required;
    }

    /**
     * ��ȡ1 ���� 0 ͣ��
     *
     * @return usable - 1 ���� 0 ͣ��
     */
    public String getUsable()
    {
        return usable;
    }

    /**
     * ����1 ���� 0 ͣ��
     *
     * @param usable
     *            1 ���� 0 ͣ��
     */
    public void setUsable(String usable)
    {
        this.usable = usable;
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