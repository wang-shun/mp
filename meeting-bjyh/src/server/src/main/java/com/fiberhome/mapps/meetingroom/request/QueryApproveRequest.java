package com.fiberhome.mapps.meetingroom.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class QueryApproveRequest extends AbstractRopRequest
{
    /** 会议室状态 0-待审批 1-已审批 */
    private String status;
    @NotNull
    private long   timestamp;
    @NotNull
    @Min(value = 1, message = "分页页数最小为1")
    private int    offset;
    @NotNull
    @Min(value = 1, message = "每页记录数最小为1")
    @Max(value = 100, message = "每页记录数最大为100")
    private int    limit;

    private int    noPage;

    private String reservedId;

    public int getNoPage()
    {
        return noPage;
    }

    public void setNoPage(int noPage)
    {
        this.noPage = noPage;
    }

    public String getReservedId()
    {
        return reservedId;
    }

    public void setReservedId(String reservedId)
    {
        this.reservedId = reservedId;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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

}
