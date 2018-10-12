package com.fiberhome.mapps.survey.entity;

import java.util.List;

public class QuestionContent
{

    private String              id;

    private String              code;

    private String              type;

    private Long                selmax;

    private Long                selmin;

    private String              txtAnswer;

    private String              required;

    private List<OptionContent> option;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Long getSelmax()
    {
        return selmax;
    }

    public void setSelmax(Long selmax)
    {
        this.selmax = selmax;
    }

    public Long getSelmin()
    {
        return selmin;
    }

    public void setSelmin(Long selmin)
    {
        this.selmin = selmin;
    }

    public String getTxtAnswer()
    {
        return txtAnswer;
    }

    public void setTxtAnswer(String txtAnswer)
    {
        this.txtAnswer = txtAnswer;
    }

    public String getRequired()
    {
        return required;
    }

    public void setRequired(String required)
    {
        this.required = required;
    }

    public List<OptionContent> getOption()
    {
        return option;
    }

    public void setOption(List<OptionContent> option)
    {
        this.option = option;
    }

}
