package com.fiberhome.mapps.meeting.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class GetLockReservedResponse extends BaseResponse
{
    @XmlElement
    private String lockId;

    public String getLockId()
    {
        return lockId;
    }

    public void setLockId(String lockId)
    {
        this.lockId = lockId;
    }

}
