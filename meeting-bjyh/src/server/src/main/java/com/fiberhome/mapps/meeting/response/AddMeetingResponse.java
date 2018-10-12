package com.fiberhome.mapps.meeting.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class AddMeetingResponse extends BaseResponse
{
    @XmlElement
    private String meetingId;
    
    @XmlElement
    private String message;

    public String getMeetingId()
    {
        return meetingId;
    }

    public void setMeetingId(String meetingId)
    {
        this.meetingId = meetingId;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
    

}
