package com.fiberhome.mapps.servicemanager.request;

import com.rop.AbstractRopRequest;

public class RetentionSaveRequest extends AbstractRopRequest {
	private String id;
	
	private String rp;
	
	private String rpName;
	
	private String retainTime;
	
	private String isDefault;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRp() {
		return rp;
	}

	public void setRp(String rp) {
		this.rp = rp;
	}

	public String getRpName() {
		return rpName;
	}

	public void setRpName(String rpName) {
		this.rpName = rpName;
	}

	public String getRetainTime() {
		return retainTime;
	}

	public void setRetainTime(String retainTime) {
		this.retainTime = retainTime;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
}
