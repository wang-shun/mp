package com.fiberhome.mapps.activity.request;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fiberhome.mapps.activity.entity.AtPrivilege;
import com.rop.AbstractRopRequest;

public class AddActivityRequest extends AbstractRopRequest
{
    @NotNull
    @Size(max = 1500, message = "活动主题最长1500个字")
    private String            content;
    @NotNull
    @Size(max = 100, message = "会议室地址最长100个字")
    private String            address;
    private String            actCoordinate;
    private Integer           numLimit;
    private String            actTitle;
    @Pattern(regexp = "^[1][3,4,5,7,8][0-9]{9}$", message = "咨询电话不合法")
    private String            conTel;
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String            phone;
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String            name;
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String            idCard;
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String            remark;
    @Pattern(regexp = "0|1", message = "格式不合法")
    private String            sex;
    @Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$", message = "日期格式不合法")
    private String            actStartTime;
    @Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$", message = "日期格式不合法")
    private String            actEndTime;
    @Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$", message = "日期格式不合法")
    private String            enterEndTime;
    private String            actPosterUrl;

    private List<String>      imageItems;
    private List<AtPrivilege> privileges;
    private String            privilegesJson;

    private String            pushFlag;

    public String getPushFlag()
    {
        return pushFlag;
    }

    public void setPushFlag(String pushFlag)
    {
        this.pushFlag = pushFlag;
    }

    public String getActCoordinate()
    {
        return actCoordinate;
    }

    public void setActCoordinate(String actCoordinate)
    {
        this.actCoordinate = actCoordinate;
    }

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

    public Integer getNumLimit()
    {
        return numLimit;
    }

    public void setNumLimit(Integer numLimit)
    {
        this.numLimit = numLimit;
    }

    public String getConTel()
    {
        return conTel;
    }

    public void setConTel(String conTel)
    {
        this.conTel = conTel;
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

    public String getActPosterUrl()
    {
        return actPosterUrl;
    }

    public void setActPosterUrl(String actPosterUrl)
    {
        this.actPosterUrl = actPosterUrl;
    }

    public List<String> getImageItems()
    {
        return imageItems;
    }

    public void setImageItems(List<String> imageItems)
    {
        this.imageItems = imageItems;
    }

    public List<AtPrivilege> getPrivileges()
    {
        return privileges;
    }

    public void setPrivileges(List<AtPrivilege> privileges)
    {
        this.privileges = privileges;
    }

    public String getPrivilegesJson()
    {
        return privilegesJson;
    }

    public void setPrivilegesJson(String privilegesJson)
    {
        this.privilegesJson = privilegesJson;
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
