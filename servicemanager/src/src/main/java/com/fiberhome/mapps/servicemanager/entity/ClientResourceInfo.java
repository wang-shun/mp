package com.fiberhome.mapps.servicemanager.entity;

import java.util.Map;

import javax.persistence.Table;

@Table(name = "sm_resource")
public class ClientResourceInfo extends SmResource {
	public ClientResourceInfo(SmResource src) {
		super();
		this.setId(src.getId());
		this.setResId(src.getResId());
		this.setName(src.getName());
		this.setRemarks(src.getRemarks());
		this.setCreator(src.getCreator());
		this.setCreateTime(src.getCreateTime());
		this.setModifier(src.getModifier());
		this.setModifyTime(src.getModifyTime());
		this.setEnabled(src.getEnabled());
	}
	
    private String resName;
    
    Map<String,String> configList;

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public Map<String, String> getConfigList() {
		return configList;
	}

	public void setConfigList(Map<String, String> configList) {
		this.configList = configList;
	}
}