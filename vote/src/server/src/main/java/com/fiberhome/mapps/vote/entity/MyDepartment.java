package com.fiberhome.mapps.vote.entity;

public class MyDepartment
{
    private String depUuid;
    private String depName;
    private String parentId;
    private String depOrder;

    public String getDepOrder()
    {
        return depOrder;
    }

    public void setDepOrder(String depOrder)
    {
        this.depOrder = depOrder;
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

}
