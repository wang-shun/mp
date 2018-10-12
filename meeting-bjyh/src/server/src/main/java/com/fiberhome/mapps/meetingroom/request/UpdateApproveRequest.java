package com.fiberhome.mapps.meetingroom.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.rop.AbstractRopRequest;

public class UpdateApproveRequest extends AbstractRopRequest
{
    @NotNull
    private String reservedId;
    /**
     * 0 不同意 1 同意
     */
    @NotNull
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String approved;

    public String getReservedId()
    {
        return reservedId;
    }

    public void setReservedId(String reservedId)
    {
        this.reservedId = reservedId;
    }

    public String getApproved()
    {
        return approved;
    }

    public void setApproved(String approved)
    {
        this.approved = approved;
    }

}
