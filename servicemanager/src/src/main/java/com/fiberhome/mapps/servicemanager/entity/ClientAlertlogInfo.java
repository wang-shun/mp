package com.fiberhome.mapps.servicemanager.entity;

import java.util.Date;

public class ClientAlertlogInfo {
	private String id;

	private String ruleId;

	private String alertLevel;

	private String message;

	private String alertData;

	private Date alertTime;
	
	private String ruleName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getAlertLevel() {
		return alertLevel;
	}

	public void setAlertLevel(String alertLevel) {
		this.alertLevel = alertLevel;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAlertData() {
		return alertData;
	}

	public void setAlertData(String alertData) {
		this.alertData = alertData;
	}

	public Date getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(Date alertTime) {
		this.alertTime = alertTime;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
}