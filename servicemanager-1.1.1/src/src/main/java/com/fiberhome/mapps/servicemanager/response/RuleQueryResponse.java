package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.McAlertRule;

public class RuleQueryResponse extends BaseResponse {
	List<McAlertRule> ruleList;
	
	long total;

	public List<McAlertRule> getRuleList() {
		return ruleList;
	}

	public void setRuleList(List<McAlertRule> ruleList) {
		this.ruleList = ruleList;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
