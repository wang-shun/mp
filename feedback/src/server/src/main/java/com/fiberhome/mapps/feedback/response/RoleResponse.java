package com.fiberhome.mapps.feedback.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class RoleResponse extends BaseResponse
{

    @XmlElement(name = "adminFlag")
    private boolean adminFlag;

    public boolean isAdminFlag()
    {
        return adminFlag;
    }

    public void setAdminFlag(boolean adminFlag)
    {
        this.adminFlag = adminFlag;
    }
}
