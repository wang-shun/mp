package com.fiberhome.mapps.trip.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.FdFeedback;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class FeedbackResponse extends BaseResponse
{
    @XmlElement
    private FdFeedback feedback;
    @XmlElement
    private String     webRoot;
    @XmlElement
    private String     deptName;
    @XmlElement
    private String     email;

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getWebRoot()
    {
        return webRoot;
    }

    public void setWebRoot(String webRoot)
    {
        this.webRoot = webRoot;
    }

    public FdFeedback getFeedback()
    {
        return feedback;
    }

    public void setFeedback(FdFeedback feedback)
    {
        this.feedback = feedback;
    }

}
