package com.fiberhome.mapps.survey.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class GetSurveyContentToWebRequest extends AbstractRopRequest
{
    @NotNull
    private String surveyId;

    @NotNull
    private String personId;

    public String getSurveyId()
    {
        return surveyId;
    }

    public void setSurveyId(String surveyId)
    {
        this.surveyId = surveyId;
    }

    public String getPersonId()
    {
        return personId;
    }

    public void setPersonId(String personId)
    {
        this.personId = personId;
    }

}
