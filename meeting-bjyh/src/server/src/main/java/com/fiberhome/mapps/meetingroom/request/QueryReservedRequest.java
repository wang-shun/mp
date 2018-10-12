package com.fiberhome.mapps.meetingroom.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.rop.AbstractRopRequest;

public class QueryReservedRequest extends AbstractRopRequest
{
    private String displayName;
    private String depName;
    private String roomId;
    /** 会议室名称 会议室地址 会议名称 模糊查询 */
    private String queryTerm;
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "日期格式不合法")
    private String reservedStartDate;
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "日期格式不合法")
    private String reservedEndDate;
    /** 会议室状态 1-准备中 2-使用中 3-已结束 4-已取消 0-已删除 */
    private String status;
    /** 排序 预定时间 1.进行降序 2.升序排序 3web自定义 */
    @Pattern(regexp = "1|2|3", message = "格式不合法")
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

    public String getSort()
    {
        return sort;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
    }

    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

    public String getQueryTerm()
    {
        return queryTerm;
    }

    public void setQueryTerm(String queryTerm)
    {
        this.queryTerm = queryTerm;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDepName()
    {
        return depName;
    }

    public void setDepName(String depName)
    {
        this.depName = depName;
    }

    public String getReservedStartDate()
    {
        return reservedStartDate;
    }

    public void setReservedStartDate(String reservedStartDate)
    {
        this.reservedStartDate = reservedStartDate;
    }

    public String getReservedEndDate()
    {
        return reservedEndDate;
    }

    public void setReservedEndDate(String reservedEndDate)
    {
        this.reservedEndDate = reservedEndDate;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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

}
