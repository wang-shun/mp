package com.fiberhome.mapps.activity.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.activity.entity.AtPrivilege;
import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryPrivilegeResponse extends BaseResponse
{
    @XmlElement(name = "privileges")
    private List<AtPrivilege> list;
    @XmlElement(name = "adminList")
    private List<AtPrivilege> adminList;
    @XmlElement(name = "serviceList")
    private List<AtPrivilege> serviceList;

    public List<AtPrivilege> getList()
    {
        return list;
    }

    public void setList(List<AtPrivilege> list)
    {
        this.list = list;
    }

    public List<AtPrivilege> getAdminList()
    {
        return adminList;
    }

    public void setAdminList(List<AtPrivilege> adminList)
    {
        this.adminList = adminList;
    }

    public List<AtPrivilege> getServiceList()
    {
        return serviceList;
    }

    public void setServiceList(List<AtPrivilege> serviceList)
    {
        this.serviceList = serviceList;
    }
}
