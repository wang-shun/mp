package com.fiberhome.mapps.survey.entity;

import java.util.Date;

public class SuQuestionOptionsJson
{

    private String id;

    private String options;
    private String optionsHtml;

    private String option;
    private String optionHtml;

    /**
     * 0 ��֧�� 1 �ı������ 2 �����
     */
    private String customInput;

    private Long   sequ;

    private String creator;

    private Date   createTime;

    private String modifier;

    private Date   modifiedTime;

    private String answer;

    private String isCheck;

    public String getOptionHtml()
    {
        return optionHtml;
    }

    public void setOptionHtml(String optionHtml)
    {
        this.optionHtml = optionHtml;
    }

    public String getOptionsHtml()
    {
        return optionsHtml;
    }

    public void setOptionsHtml(String optionsHtml)
    {
        this.optionsHtml = optionsHtml;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getOptions()
    {
        return options;
    }

    public void setOptions(String options)
    {
        this.options = options;
        this.option = options;
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

    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }

    public String getIsCheck()
    {
        return isCheck;
    }

    public void setIsCheck(String isCheck)
    {
        this.isCheck = isCheck;
    }

    public String getOption()
    {
        return option;
    }

    public void setOption(String option)
    {
        this.option = option;
    }

}