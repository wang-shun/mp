package com.fiberhome.mapps.survey.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.survey.entity.SuAnswerPeoplePojo;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class GetSurveyAnswerPeopleListResponse extends BaseResponse
{

    private long                     total;

    private String                   orgId;

    private List<SuAnswerPeoplePojo> answerPeople;

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public List<SuAnswerPeoplePojo> getAnswerPeople()
    {
        return answerPeople;
    }

    public void setAnswerPeople(List<SuAnswerPeoplePojo> answerPeople)
    {
        this.answerPeople = answerPeople;
    }

    public String getOrgId()
    {
        return orgId;
    }

    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
    }

}
