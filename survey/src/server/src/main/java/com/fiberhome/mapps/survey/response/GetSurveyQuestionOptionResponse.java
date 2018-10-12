package com.fiberhome.mapps.survey.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.survey.entity.SuQuestionOptions;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class GetSurveyQuestionOptionResponse extends BaseResponse
{
    private List<SuQuestionOptions> options;

    public List<SuQuestionOptions> getOptions()
    {
        return options;
    }

    public void setOptions(List<SuQuestionOptions> options)
    {
        this.options = options;
    }

}
