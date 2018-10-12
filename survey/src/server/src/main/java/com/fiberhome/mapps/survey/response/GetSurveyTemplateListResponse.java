package com.fiberhome.mapps.survey.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.survey.entity.SuSurveyTemplatePojo;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class GetSurveyTemplateListResponse extends BaseResponse
{

    private long                       total;

    @XmlElement(name = "surveyTemplateList")
    private List<SuSurveyTemplatePojo> surveyInfos;

    public List<SuSurveyTemplatePojo> getSurveyInfos()
    {
        return surveyInfos;
    }

    public void setSurveyInfos(List<SuSurveyTemplatePojo> surveyInfos)
    {
        this.surveyInfos = surveyInfos;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

}
