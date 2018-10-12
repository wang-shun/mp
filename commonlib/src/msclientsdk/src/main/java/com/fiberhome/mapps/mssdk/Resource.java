package com.fiberhome.mapps.mssdk;

public class Resource {
	private String resId;
	private String resCode;
	private String resName;
	private String assignedResourceId;

	public Resource() {

	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getAssignedResourceId() {
		return assignedResourceId;
	}

	public void setAssignedResourceId(String assignedResourceId) {
		this.assignedResourceId = assignedResourceId;
	}

}
