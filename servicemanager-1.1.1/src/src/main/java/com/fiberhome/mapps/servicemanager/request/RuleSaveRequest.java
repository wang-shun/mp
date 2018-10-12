package com.fiberhome.mapps.servicemanager.request;

import java.util.List;

import com.fiberhome.mapps.servicemanager.entity.McAlertCondition;
import com.fiberhome.mapps.servicemanager.entity.McAlertMethod;
import com.fiberhome.mapps.servicemanager.entity.McAlertRule;
import com.rop.AbstractRopRequest;

public class RuleSaveRequest extends AbstractRopRequest {
	private String ruleSaveJson;
	
	private McAlertRule alertRule;
	
	private List<McAlertCondition> alertConditionList;
	
	private List<McAlertMethod> alertMethodList;

	public String getRuleSaveJson() {
		return ruleSaveJson;
	}

	public void setRuleSaveJson(String ruleSaveJson) {
		this.ruleSaveJson = ruleSaveJson;
	}

	public McAlertRule getAlertRule() {
		return alertRule;
	}

	public void setAlertRule(McAlertRule alertRule) {
		this.alertRule = alertRule;
	}

	public List<McAlertCondition> getAlertConditionList() {
		return alertConditionList;
	}

	public void setAlertConditionList(List<McAlertCondition> alertConditionList) {
		this.alertConditionList = alertConditionList;
	}

	public List<McAlertMethod> getAlertMethodList() {
		return alertMethodList;
	}

	public void setAlertMethodList(List<McAlertMethod> alertMethodList) {
		this.alertMethodList = alertMethodList;
	}

}
