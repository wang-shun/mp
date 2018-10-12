package com.fiberhome.mapps.survey.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.survey.entity.SuSurveyTemplateJson;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class PreviewSurveyTemplateResponse extends BaseResponse
{
    private SuSurveyTemplateJson survey;

    public SuSurveyTemplateJson getSurvey()
    {
        return survey;
    }

    public void setSurvey(SuSurveyTemplateJson survey)
    {
        this.survey = survey;
    }

}
