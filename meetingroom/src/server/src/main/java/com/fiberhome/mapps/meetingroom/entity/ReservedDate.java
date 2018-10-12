package com.fiberhome.mapps.meetingroom.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ReservedDate
{
    @XmlElement(name = "reservedDate")
    private String             reservedDate;
    @XmlElement(name = "reservedTimes")
    private List<ReservedTime> reservedTimeList;

    public String getReservedDate()
    {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate)
    {
        this.reservedDate = reservedDate;
    }

    public List<ReservedTime> getReservedTimeList()
    {
        return reservedTimeList;
    }

    public void setReservedTimeList(List<ReservedTime> reservedTimeList)
    {
        this.reservedTimeList = reservedTimeList;
    }

}
