package com.fiberhome.mapps.meetingroom.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meetingroom.entity.GetMrRoom;
import com.fiberhome.mapps.meetingroom.entity.MrRoom;

public interface MrRoomMapper extends MyMapper<MrRoom>
{
    public int checkName(MrRoom entity);

    public List<GetMrRoom> getMrRoom(Map<String, Object> map);

    public List<GetMrRoom> getMrRoomFormWeb(Map<String, Object> map);
}