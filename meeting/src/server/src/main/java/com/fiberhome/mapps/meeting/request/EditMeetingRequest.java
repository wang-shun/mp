package com.fiberhome.mapps.meeting.request;

import java.util.List;

import com.fiberhome.mapps.meeting.entity.MdParticipants;
import com.fiberhome.mapps.meeting.entity.MtAgenda;
import com.fiberhome.mapps.meeting.entity.MtAttachment;
import com.fiberhome.mapps.meeting.entity.MtMeeting;
import com.fiberhome.mapps.meeting.entity.MtParticipants;
import com.fiberhome.mapps.meeting.entity.MtRemarks;
import com.fiberhome.mapps.meeting.entity.ClientMtSigninSequInfo;
import com.rop.AbstractRopRequest;

public class EditMeetingRequest extends AddMeetingRequest
{
    //群组
    private List<MtParticipants> signinPersonList;

	public List<MtParticipants> getSigninPersonList() {
		return signinPersonList;
	}

	public void setSigninPersonList(List<MtParticipants> signinPersonList) {
		this.signinPersonList = signinPersonList;
	}

}
