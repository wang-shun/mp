package com.fiberhome.mapps.survey.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class SaveSurveyAnswerRequest extends AbstractRopRequest
{
	@NotNull
    private String jsonStr;

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
    
}
