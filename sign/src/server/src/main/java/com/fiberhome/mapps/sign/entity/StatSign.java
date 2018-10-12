package com.fiberhome.mapps.sign.entity;

import java.util.List;

public class StatSign
{
    private Integer    signedCount;

    private Integer    noSignedCount;

    private Integer    leavesCount;

    private List<Sign> list;

    private List<Sign> signed;

    private List<Sign> unsigned;

    private List<Sign> leaves;

    public Integer getSignedCount()
    {
        return signedCount;
    }

    public void setSignedCount(Integer signedCount)
    {
        this.signedCount = signedCount;
    }

    public Integer getNoSignedCount()
    {
        return noSignedCount;
    }

    public void setNoSignedCount(Integer noSignedCount)
    {
        this.noSignedCount = noSignedCount;
    }

    public Integer getLeavesCount()
    {
        return leavesCount;
    }

    public void setLeavesCount(Integer leavesCount)
    {
        this.leavesCount = leavesCount;
    }

    public List<Sign> getList()
    {
        return list;
    }

    public void setList(List<Sign> list)
    {
        this.list = list;
    }

    public List<Sign> getSigned()
    {
        return signed;
    }

    public void setSigned(List<Sign> signed)
    {
        this.signed = signed;
    }

    public List<Sign> getUnsigned()
    {
        return unsigned;
    }

    public void setUnsigned(List<Sign> unsigned)
    {
        this.unsigned = unsigned;
    }

    public List<Sign> getLeaves()
    {
        return leaves;
    }

    public void setLeaves(List<Sign> leaves)
    {
        this.leaves = leaves;
    }
}