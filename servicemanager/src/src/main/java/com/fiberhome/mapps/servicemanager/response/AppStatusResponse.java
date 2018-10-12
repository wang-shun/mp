package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.InstanceNode;

public class AppStatusResponse extends BaseResponse {
	List<InstanceNode> nodes;
	
	public List<InstanceNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<InstanceNode> nodes) {
		this.nodes = nodes;
	}
}
