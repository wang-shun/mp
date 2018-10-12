package com.fiberhome.mapps.trip.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.rop.AbstractRopRequest;

public class QueryFeedbackRequest extends AbstractRopRequest
{
    private String userName;
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "日期格式不合法")
    private String beginDate;
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "日期格式不合法")
    private String endDate;

    @NotNull
    @Min(value = 1, message = "分页页数最小为1")
    private int    offset;
    @NotNull
    @Min(value = 1, message = "每页记录数最小为1")
    @Max(value = 100, message = "每页记录数最大为100")
    private int    limit;
    private String sort;

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

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getBeginDate()
    {
        return beginDate;
    }

    public void setBeginDate(String beginDate)
    {
        this.beginDate = beginDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

}
