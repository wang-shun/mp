package com.fiberhome.mapps.survey.entity;

public class SurveyAnswer
{

    public String  answer;

    public String  optionId;

    public String  questionId;

    private String id;

    private String answerId;

    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }

    public String getOptionId()
    {
        return optionId;
    }

    public void setOptionId(String optionId)
    {
        this.optionId = optionId;
    }

    public String getQuestionId()
    {
        return questionId;
    }

    public void setQuestionId(String questionId)
    {
        this.questionId = questionId;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getAnswerId()
    {
        return answerId;
    }

    public void setAnswerId(String answerId)
    {
        this.answerId = answerId;
    }

}
