package com.fiberhome.mapps.survey.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.survey.entity.SuSurveyPojo;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class GetSurveyListResponse extends BaseResponse
{

    private long               timestamp;

    private int                endflag;

    private long               total;

    @XmlElement(name = "surveyList")
    private List<SuSurveyPojo> surveyInfos;

    public List<SuSurveyPojo> getSurveyInfos()
    {
        return surveyInfos;
    }

    public void setSurveyInfos(List<SuSurveyPojo> surveyInfos)
    {
        this.surveyInfos = surveyInfos;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public int getEndflag()
    {
        return endflag;
    }

    public void setEndflag(int endflag)
    {
        this.endflag = endflag;
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
