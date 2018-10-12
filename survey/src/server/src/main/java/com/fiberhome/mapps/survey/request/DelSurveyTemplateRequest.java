package com.fiberhome.mapps.survey.request;

import org.hibernate.validator.constraints.NotEmpty;

import com.rop.AbstractRopRequest;

public class DelSurveyTemplateRequest extends AbstractRopRequest
{
    @NotEmpty
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
