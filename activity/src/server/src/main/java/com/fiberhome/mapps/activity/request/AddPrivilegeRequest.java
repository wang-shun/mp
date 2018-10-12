package com.fiberhome.mapps.activity.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class AddPrivilegeRequest extends AbstractRopRequest
{
    @NotNull
    private String actId;
    private String jsonData;

    public String getJsonData()
    {
        return jsonData;
    }

    public void setJsonData(String jsonData)
    {
        this.jsonData = jsonData;
    }

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}


}
