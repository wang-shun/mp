package com.fiberhome.mapps.meetingroom.entity;

public class StatisticalAnalysis
{
    /** 会议室id */
    private String roomId;
    /** 会议室名称 */
    private String roomName;
    /** 统计日期 */
    private String statDay;
    /** 预约次数 */
    private float  reservedNum;
    /** 预约总时长 */
    private float  durationSum;
    /** 每天预约平均时长 */
    private float  durationAvgDay;
    /** 每次预约平均时长 */
    private float  durationAvgNum;

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

    public String getStatDay()
    {
        return statDay;
    }

    public void setStatDay(String statDay)
    {
        this.statDay = statDay;
    }

    public float getReservedNum()
    {
        return reservedNum;
    }

    public void setReservedNum(float reservedNum)
    {
        this.reservedNum = reservedNum;
    }

    public float getDurationSum()
    {
        return durationSum;
    }

    public void setDurationSum(float durationSum)
    {
        this.durationSum = durationSum;
    }

    public float getDurationAvgDay()
    {
        return durationAvgDay;
    }

    public void setDurationAvgDay(float durationAvgDay)
    {
        this.durationAvgDay = durationAvgDay;
    }

    public float getDurationAvgNum()
    {
        return durationAvgNum;
    }

    public void setDurationAvgNum(float durationAvgNum)
    {
        this.durationAvgNum = durationAvgNum;
    }
}