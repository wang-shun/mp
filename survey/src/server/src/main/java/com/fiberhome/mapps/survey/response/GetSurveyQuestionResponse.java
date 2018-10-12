package com.fiberhome.mapps.survey.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.survey.entity.SuQuestion;
import com.fiberhome.mapps.survey.entity.SuQuestionJson;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class GetSurveyQuestionResponse extends BaseResponse
{
    private List<SuQuestionJson> questions;

    public List<SuQuestionJson> getQuestions()
    {
        return questions;
    }

    public void setQuestions(List<SuQuestionJson> questions)
    {
        this.questions = questions;
    }

}
