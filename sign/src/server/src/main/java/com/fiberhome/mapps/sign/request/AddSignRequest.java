package com.fiberhome.mapps.sign.request;

import javax.validation.constraints.NotNull;

import com.fiberhome.mapps.sign.entity.Sign;
import com.rop.AbstractRopRequest;

public class AddSignRequest extends AbstractRopRequest
{
    @NotNull
    private Sign sign;

    public Sign getSign()
    {
        return sign;
    }

    public void setSign(Sign sign)
    {
        this.sign = sign;
    }

}
