package com.fiberhome.mapps.survey.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.survey.entity.SuSurveyGroup;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class GetSurveyGroupResponse extends BaseResponse
{
    @XmlElement(name = "surveyGroups")
    private List<SuSurveyGroup> surveyGroups;

    private String              isAdmin;

    public List<SuSurveyGroup> getSurveyGroups()
    {
        return surveyGroups;
    }

    public void setSurveyGroups(List<SuSurveyGroup> surveyGroups)
    {
        this.surveyGroups = surveyGroups;
    }

    public String getIsAdmin()
    {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin)
    {
        this.isAdmin = isAdmin;
    }

}
