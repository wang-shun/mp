package com.fiberhome.mapps.meeting.dao;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meeting.entity.TmpReserved;

public interface TmpReservedMapper extends MyMapper<TmpReserved>
{
    public int checkRoomReserved(TmpReserved entity);

    public void clearLock(TmpReserved entity);

}