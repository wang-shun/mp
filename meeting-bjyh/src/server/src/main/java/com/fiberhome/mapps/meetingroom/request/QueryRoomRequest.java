package com.fiberhome.mapps.meetingroom.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.rop.AbstractRopRequest;

public class QueryRoomRequest extends AbstractRopRequest
{
    @Size(max = 30, message = "会议室名称最长30个字")
    private String roomName;
    @Size(max = 100, message = "会议室地址最长100个字")
    private String address;
    @Max(value = 1000000, message = "会场容量最大为1000000")
    private Long   capacity;
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
    @Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$", message = "日期格式不合法")
    private String reservedStartTime;
    @Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$", message = "日期格式不合法")
    private String reservedEndTime;
    @Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$", message = "日期格式不合法")
    private String reservedDate;
    /**
     * 排序 1.会场容量由高到低 2.会场容量由低到高 3.面积由高到低 4.面积由低到高 5 web端排序标识
     */
    @Pattern(regexp = "1|2|3|4|5", message = "格式不合法")
    private String order;
    @NotNull
    private long   timestamp;
    @NotNull
    @Min(value = 1, message = "分页页数最小为1")
    private int    offset;
    @NotNull
    @Min(value = 1, message = "每页记录数最小为1")
    @Max(value = 100, message = "每页记录数最大为100")
    private int    limit;

    private String sort;

    public String getReservedDate()
    {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate)
    {
        this.reservedDate = reservedDate;
    }

    public String getSort()
    {
        return sort;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
    }

    public String getReservedStartTime()
    {
        return reservedStartTime;
    }

    public void setReservedStartTime(String reservedStartTime)
    {
        this.reservedStartTime = reservedStartTime;
    }

    public String getReservedEndTime()
    {
        return reservedEndTime;
    }

    public void setReservedEndTime(String reservedEndTime)
    {
        this.reservedEndTime = reservedEndTime;
    }

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
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

    public Long getCapacity()
    {
        return capacity;
    }

    public void setCapacity(Long capacity)
    {
        this.capacity = capacity;
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
