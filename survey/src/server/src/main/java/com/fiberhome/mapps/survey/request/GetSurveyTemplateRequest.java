package com.fiberhome.mapps.survey.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class GetSurveyTemplateRequest extends AbstractRopRequest
{
    @NotNull
    private String surveyTemplateId;

    public String getSurveyTemplateId()
    {
        return surveyTemplateId;
    }

    public void setSurveyTemplateId(String surveyTemplateId)
    {
        this.surveyTemplateId = surveyTemplateId;
    }

}
