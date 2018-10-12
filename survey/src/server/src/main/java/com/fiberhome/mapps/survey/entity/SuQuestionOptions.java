package com.fiberhome.mapps.survey.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "su_question_options")
public class SuQuestionOptions
{
    @Id
    private String id;

    @Column(name = "question_id")
    private String questionId;

    private String options;
    private String optionsHtml;

    /**
     * 0 ��֧�� 1 �ı������ 2 �����
     */
    @Column(name = "custom_input")
    private String customInput;

    private Long   sequ;

    private String creator;

    @Column(name = "create_time")
    private Date   createTime;

    private String modifier;

    @Column(name = "modified_time")
    private Date   modifiedTime;

    public String getOptionsHtml()
    {
        return optionsHtml;
    }

    public void setOptionsHtml(String optionsHtml)
    {
        this.optionsHtml = optionsHtml;
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
     * @return question_id
     */
    public String getQuestionId()
    {
        return questionId;
    }

    /**
     * @param questionId
     */
    public void setQuestionId(String questionId)
    {
        this.questionId = questionId;
    }

    public String getOptions()
    {
        return options;
    }

    public void setOptions(String options)
    {
        this.options = options;
    }

    /**
     * ��ȡ0 ��֧�� 1 �ı������ 2 �����
     *
     * @return custom_input - 0 ��֧�� 1 �ı������ 2 �����
     */
    public String getCustomInput()
    {
        return customInput;
    }

    /**
     * ����0 ��֧�� 1 �ı������ 2 �����
     *
     * @param customInput
     *            0 ��֧�� 1 �ı������ 2 �����
     */
    public void setCustomInput(String customInput)
    {
        this.customInput = customInput;
    }

    /**
     * @return sequ
     */
    public Long getSequ()
    {
        return sequ;
    }

    /**
     * @param sequ
     */
    public void setSequ(Long sequ)
    {
        this.sequ = sequ;
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