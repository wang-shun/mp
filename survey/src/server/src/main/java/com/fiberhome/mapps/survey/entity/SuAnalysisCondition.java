package com.fiberhome.mapps.survey.entity;

import java.util.List;

public class SuAnalysisCondition
{

    private String                 surveyId;

    private List<SuAnalysisOption> option;

    public String getSurveyId()
    {
        return surveyId;
    }

    public void setSurveyId(String surveyId)
    {
        this.surveyId = surveyId;
    }

    public List<SuAnalysisOption> getOption()
    {
        return option;
    }

    public void setOption(List<SuAnalysisOption> option)
    {
        this.option = option;
    }

}
