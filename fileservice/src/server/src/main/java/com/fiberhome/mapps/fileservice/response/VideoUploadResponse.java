package com.fiberhome.mapps.fileservice.response;

import com.fiberhome.mos.core.openapi.rop.BaseResponse;

public class VideoUploadResponse extends BaseResponse {
	private String screenshotId;
	private String videoId;
	private String videoDuration;
	
	public String getScreenshotId() {
		return screenshotId;
	}
	public void setScreenshotId(String screenshotId) {
		this.screenshotId = screenshotId;
	}
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public String getVideoDuration() {
		return videoDuration;
	}
	public void setVideoDuration(String videoDuration) {
		this.videoDuration = videoDuration;
	}
}
