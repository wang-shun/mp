package com.fiberhome.mapps.survey.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.survey.entity.AnalysisQuestionJson;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class GetSurveyAnalysisResponse extends BaseResponse
{
    private String                     status;

    private Long                       answerPersons;

    private String                     durationStr;

    private List<AnalysisQuestionJson> analysisQuestions;

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Long getAnswerPersons()
    {
        return answerPersons;
    }

    public void setAnswerPersons(Long answerPersons)
    {
        this.answerPersons = answerPersons;
    }

    public String getDurationStr()
    {
        return durationStr;
    }

    public void setDurationStr(String durationStr)
    {
        this.durationStr = durationStr;
    }

    public List<AnalysisQuestionJson> getAnalysisQuestions()
    {
        return analysisQuestions;
    }

    public void setAnalysisQuestions(List<AnalysisQuestionJson> analysisQuestions)
    {
        this.analysisQuestions = analysisQuestions;
    }

}
