package com.fiberhome.mapps.meeting.response;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

public class GetDocUrlResponse extends BaseResponse{
	
	private String[] docUrls;

	public String[] getDocUrls() {
		return docUrls;
	}

	public void setDocUrls(String[] docUrls) {
		this.docUrls = docUrls;
	}
	
}
