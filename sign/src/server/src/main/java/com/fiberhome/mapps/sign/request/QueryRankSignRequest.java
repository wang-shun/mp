package com.fiberhome.mapps.sign.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class QueryRankSignRequest extends AbstractRopRequest
{
    @NotNull
    private String signDateBegin;
    @NotNull
    private String signDateEnd;

    public String getSignDateBegin()
    {
        return signDateBegin;
    }

    public void setSignDateBegin(String signDateBegin)
    {
        this.signDateBegin = signDateBegin;
    }

    public String getSignDateEnd()
    {
        return signDateEnd;
    }

    public void setSignDateEnd(String signDateEnd)
    {
        this.signDateEnd = signDateEnd;
    }
}
