package com.fiberhome.mapps.survey.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class AddFeedbackResponse extends BaseResponse
{
    @XmlElement
    private String FeedbackId;

    public String getFeedbackId()
    {
        return FeedbackId;
    }

    public void setFeedbackId(String feedbackId)
    {
        FeedbackId = feedbackId;
    }

}
