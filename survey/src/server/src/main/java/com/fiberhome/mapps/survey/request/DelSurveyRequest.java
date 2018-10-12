package com.fiberhome.mapps.survey.request;

import org.hibernate.validator.constraints.NotEmpty;

import com.rop.AbstractRopRequest;

public class DelSurveyRequest extends AbstractRopRequest
{
    @NotEmpty
    private String surveyId;

    public String getSurveyId()
    {
        return surveyId;
    }

    public void setSurveyId(String surveyId)
    {
        this.surveyId = surveyId;
    }

}
