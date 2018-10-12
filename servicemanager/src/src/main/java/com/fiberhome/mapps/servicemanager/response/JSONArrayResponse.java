package com.fiberhome.mapps.servicemanager.response;

import com.alibaba.fastjson.JSONArray;
import com.fiberhome.mapps.intergration.rop.BaseResponse;

public class JSONArrayResponse extends BaseResponse{
	JSONArray jsonArray;

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	
}
