package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.TraceInfo;

public class TraceInfoListResponse extends BaseResponse {
	List<TraceInfo> traceInfoList;
	
	long total;

	public List<TraceInfo> getTraceInfoList() {
		return traceInfoList;
	}

	public void setTraceInfoList(List<TraceInfo> traceInfoList) {
		this.traceInfoList = traceInfoList;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}
