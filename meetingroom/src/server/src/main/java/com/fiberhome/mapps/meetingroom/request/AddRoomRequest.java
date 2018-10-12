package com.fiberhome.mapps.meetingroom.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.rop.AbstractRopRequest;

public class AddRoomRequest extends AbstractRopRequest
{
    @NotNull
    @Size(max = 30, message = "会议室名称最长30个字")
    private String roomName;
    @NotNull
    @Size(max = 100, message = "会议室地址最长100个字")
    private String address;
    @NotNull
    @Min(value = 1, message = "会场容量最小为1")
    @Max(value = 1000000, message = "会场容量最大为1000000")
    private long   capacity;
    @NotNull
    @Min(value = 1, message = "会场面积最小为1")
    @Max(value = 1000000, message = "会场面积最大为1000000")
    private long   area;
    private String imagePath;
    private String remarks;
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String projector;
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String display;
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String microphone;
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String stereo;
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String wifi;

    public String getRoomName()
    {
        return roomName;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public long getCapacity()
    {
        return capacity;
    }

    public void setCapacity(long capacity)
    {
        this.capacity = capacity;
    }

    public long getArea()
    {
        return area;
    }

    public void setArea(long area)
    {
        this.area = area;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getProjector()
    {
        return projector;
    }

    public void setProjector(String projector)
    {
        this.projector = projector;
    }

    public String getDisplay()
    {
        return display;
    }

    public void setDisplay(String display)
    {
        this.display = display;
    }

    public String getMicrophone()
    {
        return microphone;
    }

    public void setMicrophone(String microphone)
    {
        this.microphone = microphone;
    }

    public String getStereo()
    {
        return stereo;
    }

    public void setStereo(String stereo)
    {
        this.stereo = stereo;
    }

    public String getWifi()
    {
        return wifi;
    }

    public void setWifi(String wifi)
    {
        this.wifi = wifi;
    }

}
