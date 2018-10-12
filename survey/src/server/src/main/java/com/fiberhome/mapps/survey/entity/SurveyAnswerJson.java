package com.fiberhome.mapps.survey.entity;

import java.util.Date;
import java.util.List;

public class SurveyAnswerJson
{

    public Date                beginTime;

    public String              surveyId;

    private List<SurveyAnswer> surveyAnswer;

    public Date getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(Date beginTime)
    {
        this.beginTime = beginTime;
    }

    public List<SurveyAnswer> getSurveyAnswer()
    {
        return surveyAnswer;
    }

    public void setSurveyAnswer(List<SurveyAnswer> surveyAnswer)
    {
        this.surveyAnswer = surveyAnswer;
    }

    public String getSurveyId()
    {
        return surveyId;
    }

    public void setSurveyId(String surveyId)
    {
        this.surveyId = surveyId;
    }

}
