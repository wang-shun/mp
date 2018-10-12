package com.fiberhome.mapps.activity.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.rop.AbstractRopRequest;

public class QueryActivityRequest extends AbstractRopRequest
{
    @Size(max = 1500, message = "活动主题最长1500个字")
    private String content;
    @Size(max = 100, message = "活动地址最长100个字")
    private String address;
    private String actTitle;
    @Max(value = 1000, message = "活动人数最大为1000")
    private Long   numLimit;
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String phone;
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String name;
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String idCard;
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String remark;
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String sex;
    @Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$", message = "日期格式不合法")
    private String actStartTime;
    @Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$", message = "日期格式不合法")
    private String actEndTime;
    @Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$", message = "日期格式不合法")
    private String enterEndTime;
    /**
     * 排序 1.综合 2.即将开始 3.最新发布 4.人气最高 5.我参与的 6.我创建的
     */
    @NotNull
    @Pattern(regexp = "1|2|3|4|5|6", message = "格式不合法")
    private String order;
    /**
     * 是否往期 1 是 0 否
     */
    @Pattern(regexp = "1|0", message = "格式不合法")
    private String actExpire;

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

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Long getNumLimit()
    {
        return numLimit;
    }

    public void setNumLimit(Long numLimit)
    {
        this.numLimit = numLimit;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getActStartTime()
    {
        return actStartTime;
    }

    public void setActStartTime(String actStartTime)
    {
        this.actStartTime = actStartTime;
    }

    public String getActEndTime()
    {
        return actEndTime;
    }

    public void setActEndTime(String actEndTime)
    {
        this.actEndTime = actEndTime;
    }

    public String getEnterEndTime()
    {
        return enterEndTime;
    }

    public void setEnterEndTime(String enterEndTime)
    {
        this.enterEndTime = enterEndTime;
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

    public String getSort()
    {
        return sort;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
    }

    public String getActExpire()
    {
        return actExpire;
    }

    public void setActExpire(String actExpire)
    {
        this.actExpire = actExpire;
    }

    public String getActTitle()
    {
        return actTitle;
    }

    public void setActTitle(String actTitle)
    {
        this.actTitle = actTitle;
    }

}
