package com.fiberhome.mapps.dbr.request;

import com.rop.AbstractRopRequest;

public class QueryDeviceRequest extends AbstractRopRequest
{
    private String deviceId;
    private String deviceName;
    private String deviceStatus;

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

    public String getDeviceStatus()
    {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus)
    {
        this.deviceStatus = deviceStatus;
    }

}
