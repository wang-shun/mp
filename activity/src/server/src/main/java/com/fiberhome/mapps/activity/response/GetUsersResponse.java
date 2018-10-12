package com.fiberhome.mapps.activity.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class GetUsersResponse extends BaseResponse
{
    @XmlElement(name = "userList")
    private List<MyUser> userInfos;

    public List<MyUser> getUserInfos()
    {
        return userInfos;
    }

    public void setUserInfos(List<MyUser> userInfos)
    {
        this.userInfos = userInfos;
    }

}
