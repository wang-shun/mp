package com.fiberhome.mapps.meetingroom.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.rop.AbstractRopRequest;

public class QueryRoomFromWebRequest extends AbstractRopRequest
{
    @Size(max = 30, message = "会议室名称最长30个字")
    private String roomName;
    @Size(max = 100, message = "会议室地址最长100个字")
    private String address;
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
    @NotNull
    @Min(value = 1, message = "分页页数最小为1")
    private int    offset;
    @NotNull
    @Min(value = 1, message = "每页记录数最小为1")
    @Max(value = 100, message = "每页记录数最大为100")
    private int    limit;

    private String sort;

    private String roomType;

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

    public int getOffset()
    {
        return offset;
    }

    public void setOffset(int offset)
    {
        this.offset = offset;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    public String getSort()
    {
        return sort;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
    }

    public String getRoomType()
    {
        return roomType;
    }

    public void setRoomType(String roomType)
    {
        this.roomType = roomType;
    }

}
