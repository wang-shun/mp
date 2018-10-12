package com.fiberhome.mapps.servicemanager.response;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.McRetentionPolicy;

public class RetentionDetailResponse extends BaseResponse {
	McRetentionPolicy retentionPolicyDetail;

	public McRetentionPolicy getRetentionPolicyDetail() {
		return retentionPolicyDetail;
	}

	public void setRetentionPolicyDetail(McRetentionPolicy retentionPolicyDetail) {
		this.retentionPolicyDetail = retentionPolicyDetail;
	}
}
