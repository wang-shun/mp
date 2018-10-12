package com.fiberhome.mapps.dbr.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.dbr.entity.GetDeviceInfo;
import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryDeviceResponse extends BaseResponse
{
    @XmlElement(name = "deviceList")
    private List<GetDeviceInfo> list;

    public List<GetDeviceInfo> getList()
    {
        return list;
    }

    public void setList(List<GetDeviceInfo> list)
    {
        this.list = list;
    }

}
