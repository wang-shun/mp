package com.fiberhome.mapps.ydzf.entity;

import java.util.Date;
import java.util.List;

import com.fiberhome.mapps.ydzf.utils.DateUtil;

public class LawSampling {
	private String samplingId;
	
	private	String declNo;
	
	private Date samplingTime;
	
	private String samplingTimeStr;
	
	private String samplingAddr;
	
	private List<LawSamplingList> lawSamplingLists;
	public String getSamplingId() {
		return samplingId;
	}

	public void setSamplingId(String samplingId) {
		this.samplingId = samplingId;
	}

	public String getDeclNo() {
		return declNo;
	}

	public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}

	public Date getSamplingTime() {
		return samplingTime;
	}

	public void setSamplingTime(Date samplingTime) {
		this.samplingTime = samplingTime;
		if(samplingTime!=null){
			setSamplingTimeStr(DateUtil.sdf.format(samplingTime));
		}
	}

	public String getSamplingAddr() {
		return samplingAddr;
	}

	public void setSamplingAddr(String samplingAddr) {
		this.samplingAddr = samplingAddr;
	}

	public String getSamplingTimeStr() {
		return samplingTimeStr;
	}

	public void setSamplingTimeStr(String samplingTimeStr) {
		this.samplingTimeStr = samplingTimeStr;
	}

	public List<LawSamplingList> getLawSamplingLists() {
		return lawSamplingLists;
	}

	public void setLawSamplingLists(List<LawSamplingList> lawSamplingLists) {
		this.lawSamplingLists = lawSamplingLists;
	}
	
}
