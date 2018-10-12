package com.fiberhome.mapps.activity.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class AddEnterRequest extends AbstractRopRequest
{
    @NotNull
    private String actId;
    private String phone;
    private String idCard;
    private String remark;
    private String sex;

    public String getActId()
    {
        return actId;
    }

    public void setActId(String actId)
    {
        this.actId = actId;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
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

}
