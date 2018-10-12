package com.fiberhome.mapps.trip.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class AddFeedbackRequest extends AbstractRopRequest
{
    @NotNull
    private String       appId;
    @NotNull
    private String       appName;
    @NotNull
    private String       appVer;
    private String       appVerFmt;
    private String       deviceId;
    private String       deviceName;
    private String       osVer;
    @NotNull
    private String       feedback;
    private String       images;
    private String       contack;
    private List<String> imageItems;

    public List<String> getImageItems()
    {
        return imageItems;
    }

    public void setImageItems(List<String> imageItems)
    {
        this.imageItems = imageItems;
    }

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getAppName()
    {
        return appName;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getAppVer()
    {
        return appVer;
    }

    public void setAppVer(String appVer)
    {
        this.appVer = appVer;
    }

    public String getAppVerFmt()
    {
        return appVerFmt;
    }

    public void setAppVerFmt(String appVerFmt)
    {
        this.appVerFmt = appVerFmt;
    }

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

    public String getOsVer()
    {
        return osVer;
    }

    public void setOsVer(String osVer)
    {
        this.osVer = osVer;
    }

    public String getFeedback()
    {
        return feedback;
    }

    public void setFeedback(String feedback)
    {
        this.feedback = feedback;
    }

    public String getImages()
    {
        return images;
    }

    public void setImages(String images)
    {
        this.images = images;
    }

    public String getContack()
    {
        return contack;
    }

    public void setContack(String contack)
    {
        this.contack = contack;
    }

}
