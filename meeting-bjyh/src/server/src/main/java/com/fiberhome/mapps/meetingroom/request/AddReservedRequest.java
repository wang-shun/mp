package com.fiberhome.mapps.meetingroom.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.rop.AbstractRopRequest;

public class AddReservedRequest extends AbstractRopRequest
{
    @NotNull
    private String roomId;
    @NotNull
    @Size(max = 20, message = "会议名称最长20个字")
    private String meetingName;
    @NotNull
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "日期格式不合法")
    private String reservedDate;
    @NotNull
    @Pattern(regexp = "^\\d{2}\\s*:\\s*\\d{2}$", message = "时间格式不合法")
    private String reservedStartTime;
    @NotNull
    @Pattern(regexp = "^\\d{2}\\s*:\\s*\\d{2}$", message = "时间格式不合法")
    private String reservedEndTime;
    private String reservedRemark;

    private String meetingId;

    public String getMeetingId()
    {
        return meetingId;
    }

    public void setMeetingId(String meetingId)
    {
        this.meetingId = meetingId;
    }

    public String getReservedRemark()
    {
        return reservedRemark;
    }

    public void setReservedRemark(String reservedRemark)
    {
        this.reservedRemark = reservedRemark;
    }

    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

    public String getMeetingName()
    {
        return meetingName;
    }

    public void setMeetingName(String meetingName)
    {
        this.meetingName = meetingName;
    }

    public String getReservedDate()
    {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate)
    {
        this.reservedDate = reservedDate;
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

}
