package com.fiberhome.mapps.meeting.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.meeting.entity.ClientMeetingInfo;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryMeetingResponse extends BaseResponse
{
    @XmlElement(name = "meeting")
    private List<ClientMeetingInfo> meetingList;

    @XmlElement(name = "timestamp")
    private long                    timestamp;

    @XmlElement(name = "endflag")
    private int                     endflag;

    @XmlElement(name = "total")
    private long                    total;

    @XmlElement(name = "reservedFlag")
    private int                     reservedFlag;

    @XmlElement(name = "user")
    private MyUser                  myUser;

    @XmlElement(name = "adminFlag")
    private int                     adminFlag;

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

    public List<ClientMeetingInfo> getMeetingList()
    {
        return meetingList;
    }

    public void setMeetingList(List<ClientMeetingInfo> meetingList)
    {
        this.meetingList = meetingList;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public int getEndflag()
    {
        return endflag;
    }

    public void setEndflag(int endflag)
    {
        this.endflag = endflag;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }
}
