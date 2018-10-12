package com.fiberhome.mapps.dbr.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class AddDeviceRequest extends AbstractRopRequest
{
    @NotNull
    private String deviceId;
    @NotNull
    private String deviceName;

    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

}
