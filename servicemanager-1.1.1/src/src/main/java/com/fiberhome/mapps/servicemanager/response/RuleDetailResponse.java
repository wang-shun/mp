package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.McAlertCondition;
import com.fiberhome.mapps.servicemanager.entity.McAlertMethod;
import com.fiberhome.mapps.servicemanager.entity.McAlertRule;

public class RuleDetailResponse extends BaseResponse {
	private McAlertRule alertRule;
	
	private List<McAlertCondition> alertConditionList;
	
	private List<McAlertMethod> alertMethodList;

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
