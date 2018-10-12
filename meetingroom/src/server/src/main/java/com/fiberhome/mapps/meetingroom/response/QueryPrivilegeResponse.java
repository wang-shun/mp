package com.fiberhome.mapps.meetingroom.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.meetingroom.entity.MrPrivilege;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryPrivilegeResponse extends BaseResponse
{
    @XmlElement(name = "privileges")
    private List<MrPrivilege> list;
    @XmlElement(name = "adminList")
    private List<MrPrivilege> adminList;
    @XmlElement(name = "serviceList")
    private List<MrPrivilege> serviceList;

    public List<MrPrivilege> getList()
    {
        return list;
    }

    public void setList(List<MrPrivilege> list)
    {
        this.list = list;
    }

    public List<MrPrivilege> getAdminList()
    {
        return adminList;
    }

    public void setAdminList(List<MrPrivilege> adminList)
    {
        this.adminList = adminList;
    }

    public List<MrPrivilege> getServiceList()
    {
        return serviceList;
    }

    public void setServiceList(List<MrPrivilege> serviceList)
    {
        this.serviceList = serviceList;
    }
}
