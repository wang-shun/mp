package com.fiberhome.mapps.survey.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.survey.entity.SuSurveyJson;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class PreviewSurveyResponse extends BaseResponse
{
    private SuSurveyJson survey;

    public SuSurveyJson getSurvey()
    {
        return survey;
    }

    public void setSurvey(SuSurveyJson survey)
    {
        this.survey = survey;
    }

}
