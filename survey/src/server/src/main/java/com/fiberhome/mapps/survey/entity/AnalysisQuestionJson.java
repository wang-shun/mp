package com.fiberhome.mapps.survey.entity;

import java.util.List;

public class AnalysisQuestionJson
{
    private String                           id;

    private String                           code;

    private String                           question;

    /**
     * 1 ��ѡ 2 ��ѡ 3 ���� 4 �ı� 5 �����ı�
     */
    private String                           type;

    private String                           required;

    private Long                             sequ;

    private String                           total;

    public List<AnalysisQuestionOptionsJson> suQustionOptions;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
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

    public Long getSequ()
    {
        return sequ;
    }

    public void setSequ(Long sequ)
    {
        this.sequ = sequ;
    }

    public String getTotal()
    {
        return total;
    }

    public void setTotal(String total)
    {
        this.total = total;
    }

    public List<AnalysisQuestionOptionsJson> getSuQustionOptions()
    {
        return suQustionOptions;
    }

    public void setSuQustionOptions(List<AnalysisQuestionOptionsJson> suQustionOptions)
    {
        this.suQustionOptions = suQustionOptions;
    }

}