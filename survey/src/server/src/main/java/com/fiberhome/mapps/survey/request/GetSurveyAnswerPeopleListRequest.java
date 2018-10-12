package com.fiberhome.mapps.survey.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class GetSurveyAnswerPeopleListRequest extends AbstractRopRequest
{
    @NotNull
    private String surveyId;

    private int    offset;

    private int    limit;

    private String sort;

    public int getOffset()
    {
        return offset;
    }

    public void setOffset(int offset)
    {
        this.offset = offset;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    public String getSort()
    {
        return sort;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
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
