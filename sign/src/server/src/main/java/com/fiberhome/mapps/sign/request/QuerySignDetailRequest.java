package com.fiberhome.mapps.sign.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class QuerySignDetailRequest extends AbstractRopRequest
{
    @NotNull
    private String id;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
