package com.fiberhome.mapps.meetingroom.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.meeting.entity.ReservedDate;
import com.fiberhome.mapps.meeting.entity.ReservedInfo;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryRoomDetailResponse extends BaseResponse
{
    @XmlElement(name = "roomId")
    private String             roomId;
    @XmlElement(name = "roomName")
    private String             roomName;
    @XmlElement(name = "address")
    private String             address;
    @XmlElement(name = "capacity")
    private Long               capacity;
    @XmlElement(name = "area")
    private Long               area;
    @XmlElement(name = "imagePath")
    private String             imagePath;
    @XmlElement(name = "layout")
    private String             layout;
    @XmlElement(name = "remarks")
    private String             remarks;
    @XmlElement(name = "projector")
    private String             projector;
    @XmlElement(name = "display")
    private String             display;
    @XmlElement(name = "microphone")
    private String             microphone;
    @XmlElement(name = "stereo")
    private String             stereo;
    @XmlElement(name = "wifi")
    private String             wifi;
    @XmlElement(name = "collection")
    private String             collection;
    @XmlElement(name = "reservedDates")
    private List<ReservedDate> reservedDateList;
    @XmlElement(name = "userName")
    private String             userName;
    @XmlElement(name = "userPhone")
    private String             userPhone;
    @XmlElement(name = "needApprove")
    private String             needApprove;
    @XmlElement(name = "adminName")
    private String             adminName;
    @XmlElement(name = "serviceName")
    private String             serviceName;
    @XmlElement(name = "reservedInfos")
    private List<ReservedInfo> reservedList;

    public List<ReservedInfo> getReservedList()
    {
        return reservedList;
    }

    public void setReservedList(List<ReservedInfo> reservedList)
    {
        this.reservedList = reservedList;
    }

    public String getAdminName()
    {
        return adminName;
    }

    public void setAdminName(String adminName)
    {
        this.adminName = adminName;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    public String getNeedApprove()
    {
        return needApprove;
    }

    public void setNeedApprove(String needApprove)
    {
        this.needApprove = needApprove;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserPhone()
    {
        return userPhone;
    }

    public void setUserPhone(String userPhone)
    {
        this.userPhone = userPhone;
    }

    public String getLayout()
    {
        return layout;
    }

    public void setLayout(String layout)
    {
        this.layout = layout;
    }

    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Long getCapacity()
    {
        return capacity;
    }

    public void setCapacity(Long capacity)
    {
        this.capacity = capacity;
    }

    public Long getArea()
    {
        return area;
    }

    public void setArea(Long area)
    {
        this.area = area;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getProjector()
    {
        return projector;
    }

    public void setProjector(String projector)
    {
        this.projector = projector;
    }

    public String getDisplay()
    {
        return display;
    }

    public void setDisplay(String display)
    {
        this.display = display;
    }

    public String getMicrophone()
    {
        return microphone;
    }

    public void setMicrophone(String microphone)
    {
        this.microphone = microphone;
    }

    public String getStereo()
    {
        return stereo;
    }

    public void setStereo(String stereo)
    {
        this.stereo = stereo;
    }

    public String getWifi()
    {
        return wifi;
    }

    public void setWifi(String wifi)
    {
        this.wifi = wifi;
    }

    public String getCollection()
    {
        return collection;
    }

    public void setCollection(String collection)
    {
        this.collection = collection;
    }

    public List<ReservedDate> getReservedDateList()
    {
        return reservedDateList;
    }

    public void setReservedDateList(List<ReservedDate> reservedDateList)
    {
        this.reservedDateList = reservedDateList;
    }

}
