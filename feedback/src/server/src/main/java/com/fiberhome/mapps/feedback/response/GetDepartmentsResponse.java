package com.fiberhome.mapps.feedback.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.contact.pojo.MyDepartment;
import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class GetDepartmentsResponse extends BaseResponse
{
    @XmlElement(name = "depList")
    private List<MyDepartment> departmentInfos;

    public List<MyDepartment> getDepartmentInfos()
    {
        return departmentInfos;
    }

    public void setDepartmentInfos(List<MyDepartment> departmentInfos)
    {
        this.departmentInfos = departmentInfos;
    }
}
