package com.fiberhome.mapps.survey.request;

import org.hibernate.validator.constraints.NotEmpty;

import com.rop.AbstractRopRequest;

public class SaveSurveyGroupRequest extends AbstractRopRequest
{
    @NotEmpty
    private String groupName;

    private String groupId;

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

}
