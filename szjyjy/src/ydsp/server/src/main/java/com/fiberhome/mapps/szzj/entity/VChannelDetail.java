package com.fiberhome.mapps.szzj.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class VChannelDetail {
	private String  hostId;
	
	private String channel;
	
	private int capacity;
	
	private String visibility;
	
	private String meetingId;
	
	private String hostPass;
	
	private String userCode;
	
	private String hostEmail;
	
	private Date createTime;
	
	private String createTimeString;
	
	private String meetings;
	
	private String meetingMinutes;
	
	private String status;

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	} 
	

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	public String getHostPass() {
		return hostPass;
	}

	public void setHostPass(String hostPass) {
		this.hostPass = hostPass;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getHostEmail() {
		return hostEmail;
	}

	public void setHostEmail(String hostEmail) {
		this.hostEmail = hostEmail;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
		if(createTime!=null){
			setCreateTimeString(df.format(createTime));
		}
	}

	public String getCreateTimeString() {
		return createTimeString;
	}

	public void setCreateTimeString(String createTimeString) {
		this.createTimeString = createTimeString;
	}

	public String getMeetings() {
		return meetings;
	}

	public void setMeetings(String meetings) {
		this.meetings = meetings;
	}

	public String getMeetingMinutes() {
		return meetingMinutes;
	}

	public void setMeetingMinutes(String meetingMinutes) {
		this.meetingMinutes = meetingMinutes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
