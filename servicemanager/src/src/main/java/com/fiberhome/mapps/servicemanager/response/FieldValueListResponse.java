package com.fiberhome.mapps.servicemanager.response;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.entity.McValueField;

public class FieldValueListResponse extends BaseResponse {
	List<McValueField> list;

	public List<McValueField> getList() {
		return list;
	}

	public void setList(List<McValueField> list) {
		this.list = list;
	}
}
