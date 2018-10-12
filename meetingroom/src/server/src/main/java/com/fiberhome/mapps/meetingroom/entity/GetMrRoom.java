package com.fiberhome.mapps.meetingroom.entity;

import javax.persistence.Id;

public class GetMrRoom
{
    @Id
    private String roomId;

    private String ecid;

    private String roomName;

    private String address;

    /**
     * m2
     */
    private Long   area;

    /**
     * 人
     */
    private Long   capacity;

    /**
     * 是否有（1 是/0 否）
     */
    private String projector;

    /**
     * 是否有（1 是/0 否）
     */
    private String display;

    /**
     * 是否有（1 是/0 否）
     */
    private String microphone;

    /**
     * 是否有（1 是/0 否）
     */
    private String stereo;

    /**
     * 是否有（1 是/0 否）
     */
    private String wifi;

    /**
     * 布局图文件ID
     */
    private String layout;

    /**
     * 其他说明
     */
    private String remarks;

    /**
     * 是否启用
     */
    private String status;

    private String collection;

    /**
     * 会议室类型 0 普通 1 特殊
     */
    private String roomType;

    public String getRoomType()
    {
        return roomType;
    }

    public void setRoomType(String roomType)
    {
        this.roomType = roomType;
    }

    /**
     * @return ecid
     */
    public String getEcid()
    {
        return ecid;
    }

    /**
     * @param ecid
     */
    public void setEcid(String ecid)
    {
        this.ecid = ecid;
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

    public String getCollection()
    {
        return collection;
    }

    public void setCollection(String collection)
    {
        this.collection = collection;
    }

    /**
     * @return address
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * 获取m2
     *
     * @return area - m2
     */
    public Long getArea()
    {
        return area;
    }

    /**
     * 设置m2
     *
     * @param area m2
     */
    public void setArea(Long area)
    {
        this.area = area;
    }

    /**
     * 获取人
     *
     * @return capacity - 人
     */
    public Long getCapacity()
    {
        return capacity;
    }

    /**
     * 设置人
     *
     * @param capacity 人
     */
    public void setCapacity(Long capacity)
    {
        this.capacity = capacity;
    }

    /**
     * 获取是否有（1 是/0 否）
     *
     * @return projector - 是否有（1 是/0 否）
     */
    public String getProjector()
    {
        return projector;
    }

    /**
     * 设置是否有（1 是/0 否）
     *
     * @param projector 是否有（1 是/0 否）
     */
    public void setProjector(String projector)
    {
        this.projector = projector;
    }

    /**
     * 获取是否有（1 是/0 否）
     *
     * @return display - 是否有（1 是/0 否）
     */
    public String getDisplay()
    {
        return display;
    }

    /**
     * 设置是否有（1 是/0 否）
     *
     * @param display 是否有（1 是/0 否）
     */
    public void setDisplay(String display)
    {
        this.display = display;
    }

    /**
     * 获取是否有（1 是/0 否）
     *
     * @return microphone - 是否有（1 是/0 否）
     */
    public String getMicrophone()
    {
        return microphone;
    }

    /**
     * 设置是否有（1 是/0 否）
     *
     * @param microphone 是否有（1 是/0 否）
     */
    public void setMicrophone(String microphone)
    {
        this.microphone = microphone;
    }

    /**
     * 获取是否有（1 是/0 否）
     *
     * @return stereo - 是否有（1 是/0 否）
     */
    public String getStereo()
    {
        return stereo;
    }

    /**
     * 设置是否有（1 是/0 否）
     *
     * @param stereo 是否有（1 是/0 否）
     */
    public void setStereo(String stereo)
    {
        this.stereo = stereo;
    }

    /**
     * 获取是否有（1 是/0 否）
     *
     * @return wifi - 是否有（1 是/0 否）
     */
    public String getWifi()
    {
        return wifi;
    }

    /**
     * 设置是否有（1 是/0 否）
     *
     * @param wifi 是否有（1 是/0 否）
     */
    public void setWifi(String wifi)
    {
        this.wifi = wifi;
    }

    /**
     * 获取布局图文件ID
     *
     * @return layout - 布局图文件ID
     */
    public String getLayout()
    {
        return layout;
    }

    /**
     * 设置布局图文件ID
     *
     * @param layout 布局图文件ID
     */
    public void setLayout(String layout)
    {
        this.layout = layout;
    }

    /**
     * 获取其他说明
     *
     * @return remarks - 其他说明
     */
    public String getRemarks()
    {
        return remarks;
    }

    /**
     * 设置其他说明
     *
     * @param remarks 其他说明
     */
    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    /**
     * 获取是否启用
     *
     * @return status - 是否启用
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * 设置是否启用
     *
     * @param status 是否启用
     */
    public void setStatus(String status)
    {
        this.status = status;
    }
}