package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.McRetentionPolicy;

public class RetentionQueryResponse extends BaseResponse {
	List<McRetentionPolicy> retentionList;
	
	long total;

	public List<McRetentionPolicy> getRetentionList() {
		return retentionList;
	}

	public void setRetentionList(List<McRetentionPolicy> retentionList) {
		this.retentionList = retentionList;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}
