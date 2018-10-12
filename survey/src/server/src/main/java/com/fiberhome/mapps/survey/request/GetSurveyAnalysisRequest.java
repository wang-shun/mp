package com.fiberhome.mapps.survey.request;

import com.rop.AbstractRopRequest;

public class GetSurveyAnalysisRequest extends AbstractRopRequest
{
    private String jsonStr;

    public String getJsonStr()
    {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr)
    {
        this.jsonStr = jsonStr;
    }

}
