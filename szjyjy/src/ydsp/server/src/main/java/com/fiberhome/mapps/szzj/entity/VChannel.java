package com.fiberhome.mapps.szzj.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "v_channel")
public class VChannel {
	@Id
	@Column(name = "host_id")
	private String  hostId;
	
	@Column(name = "channel")
	private String channel;
	
	@Column(name = "capacity")
	private Integer capacity;
	
	@Column(name = "visibility")
	private String visibility;
	
	@Column(name = "meeting_id")
	private String meetingId;
	
	@Column(name = "host_pass")
	private String hostPass;
	
	@Column(name = "user_code")
	private String userCode;
	
	@Column(name = "host_email")
	private String hostEmail;
	
	@Column(name = "create_time")
	private Date createTime;

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

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
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
	}
}
