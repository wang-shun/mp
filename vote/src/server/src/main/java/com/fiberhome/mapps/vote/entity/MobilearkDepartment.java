package com.fiberhome.mapps.vote.entity;

public class MobilearkDepartment
{
    private String  depUuid;
    private String  depName;
    private String  parentId;
    private String  email;
    private String  total;
    private Integer depWeight;
    private Long    updateTime;
    private Integer mode;
    private String  depOrder;

    public String getDepOrder()
    {
        return depOrder;
    }

    public void setDepOrder(String depOrder)
    {
        this.depOrder = depOrder;
    }

    public Integer getDepWeight()
    {
        return depWeight;
    }

    public void setDepWeight(Integer depWeight)
    {
        this.depWeight = depWeight;
    }

    public String getDepUuid()
    {
        return depUuid;
    }

    public void setDepUuid(String depUuid)
    {
        this.depUuid = depUuid;
    }

    public String getDepName()
    {
        return depName;
    }

    public void setDepName(String depName)
    {
        this.depName = depName;
    }

    public String getParentId()
    {
        return parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getTotal()
    {
        return total;
    }

    public void setTotal(String total)
    {
        this.total = total;
    }

    public Long getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime)
    {
        this.updateTime = updateTime;
    }

    public Integer getMode()
    {
        return mode;
    }

    public void setMode(Integer mode)
    {
        this.mode = mode;
    }

}
