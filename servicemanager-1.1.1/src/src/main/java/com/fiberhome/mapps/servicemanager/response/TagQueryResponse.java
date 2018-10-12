package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.McTag;

public class TagQueryResponse extends BaseResponse {
	List<McTag> tagList;
	
	long total;

	public List<McTag> getTagList() {
		return tagList;
	}

	public void setTagList(List<McTag> tagList) {
		this.tagList = tagList;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
