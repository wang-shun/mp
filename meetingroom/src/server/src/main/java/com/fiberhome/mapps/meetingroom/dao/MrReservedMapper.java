package com.fiberhome.mapps.meetingroom.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meetingroom.entity.GetMrReserved;
import com.fiberhome.mapps.meetingroom.entity.MrReserved;
import com.fiberhome.mapps.meetingroom.entity.ReservedDate;
import com.fiberhome.mapps.meetingroom.entity.ReservedDetail;
import com.fiberhome.mapps.meetingroom.entity.ReservedStatus;
import com.fiberhome.mapps.meetingroom.entity.ReservedTime;
import com.fiberhome.mapps.meetingroom.entity.Room;

public interface MrReservedMapper extends MyMapper<MrReserved>
{
    public int checkRoomReserved(MrReserved entity);

    public List<ReservedDate> getReservedDates(Map<String, Object> map);

    public List<ReservedTime> getReservedTimes(Map<String, Object> map);

    public List<ReservedDate> getReservedDatesV20(Map<String, Object> map);

    public List<ReservedTime> getReservedTimesV20(Map<String, Object> map);

    public List<GetMrReserved> getMrReserved(Map<String, Object> map);

    public List<GetMrReserved> getMrReservedV20(Map<String, Object> map);

    public int getReservedCountByUser(MrReserved entity);

    public void updateStatusSchedule(Map<String, Object> map);

    public void updateApproveStatusSchedule(Map<String, Object> map);

    public List<MrReserved> getReservedByDay(Map<String, Object> map);

    public List<ReservedDetail> getReservedDetail(Map<String, Object> map);

    public List<GetMrReserved> getReviewForReserved(Map<String, Object> map);

    public ReservedStatus getReservedStatus(Map<String, Object> map);

    public ReservedDetail getReservedDetailOne(Map<String, Object> map);

    public List<Room> getRoomByDo(Map<String, Object> map);

    public List<MrReserved> selectApproveStatusSchedule(Map<String, Object> map);

    public void clearData(Map<String, Object> map);
}