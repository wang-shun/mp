package com.fiberhome.mapps.meetingroom.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.meetingroom.entity.ReservedDetail;
import com.fiberhome.mapps.meetingroom.entity.Room;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryResponse extends BaseResponse
{
    @XmlElement(name = "reservedDetailList")
    private List<ReservedDetail> reservedDetailList;
    @XmlElement(name = "roomList")
    private List<Room>           roomList;

    public List<Room> getRoomList()
    {
        return roomList;
    }

    public void setRoomList(List<Room> roomList)
    {
        this.roomList = roomList;
    }

    public List<ReservedDetail> getReservedDetailList()
    {
        return reservedDetailList;
    }

    public void setReservedDetailList(List<ReservedDetail> reservedDetailList)
    {
        this.reservedDetailList = reservedDetailList;
    }
}
