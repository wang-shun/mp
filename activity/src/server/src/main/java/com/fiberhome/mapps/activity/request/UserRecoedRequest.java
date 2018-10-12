package com.fiberhome.mapps.activity.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.rop.AbstractRopRequest;

public class UserRecoedRequest extends AbstractRopRequest
{
    private String userName;
    private String depName;
    private String op;
    @NotNull
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "日期格式不合法")
    private String statBeginTime;
    /** 分页标识 1 分页 2 不分页 */
    @NotNull
    @Pattern(regexp = "[1-2]{1}", message = "格式不合法")
    private String pageFlag;

    public String getPageFlag()
    {
        return pageFlag;
    }

    public void setPageFlag(String pageFlag)
    {
        this.pageFlag = pageFlag;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getDepName()
    {
        return depName;
    }

    public void setDepName(String depName)
    {
        this.depName = depName;
    }

    public String getOp()
    {
        return op;
    }

    public void setOp(String op)
    {
        this.op = op;
    }

    @NotNull
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "日期格式不合法")
    private String statEndTime;

    private int    offset;
    private int    limit;

    public String getStatBeginTime()
    {
        return statBeginTime;
    }

    public void setStatBeginTime(String statBeginTime)
    {
        this.statBeginTime = statBeginTime;
    }

    public String getStatEndTime()
    {
        return statEndTime;
    }

    public void setStatEndTime(String statEndTime)
    {
        this.statEndTime = statEndTime;
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
