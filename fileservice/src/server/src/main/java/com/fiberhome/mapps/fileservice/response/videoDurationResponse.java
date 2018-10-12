package com.fiberhome.mapps.fileservice.response;

import com.fiberhome.mos.core.openapi.rop.BaseResponse;

public class videoDurationResponse extends BaseResponse {
	private String videoDuration;

	public String getVideoDuration() {
		return videoDuration;
	}

	public void setVideoDuration(String videoDuration) {
		this.videoDuration = videoDuration;
	}
}
