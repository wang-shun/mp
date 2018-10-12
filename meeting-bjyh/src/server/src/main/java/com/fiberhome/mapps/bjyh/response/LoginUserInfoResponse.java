package com.fiberhome.mapps.bjyh.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class LoginUserInfoResponse extends BaseResponse
{
    @XmlElement(name = "reservedFlag")
    private int    reservedFlag;

    @XmlElement(name = "user")
    private MyUser myUser;

    @XmlElement(name = "adminFlag")
    private int    adminFlag;

    public int getAdminFlag()
    {
        return adminFlag;
    }

    public void setAdminFlag(int adminFlag)
    {
        this.adminFlag = adminFlag;
    }

    public MyUser getMyUser()
    {
        return myUser;
    }

    public void setMyUser(MyUser myUser)
    {
        this.myUser = myUser;
    }

    public int getReservedFlag()
    {
        return reservedFlag;
    }

    public void setReservedFlag(int reservedFlag)
    {
        this.reservedFlag = reservedFlag;
    }
}
