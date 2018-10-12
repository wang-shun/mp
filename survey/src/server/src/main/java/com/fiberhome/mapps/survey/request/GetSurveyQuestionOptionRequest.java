package com.fiberhome.mapps.survey.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class GetSurveyQuestionOptionRequest extends AbstractRopRequest
{
    @NotNull
    private String questionId;

    public String getQuestionId()
    {
        return questionId;
    }

    public void setQuestionId(String questionId)
    {
        this.questionId = questionId;
    }

}
