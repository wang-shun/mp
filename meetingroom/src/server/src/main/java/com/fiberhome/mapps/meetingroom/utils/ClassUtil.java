package com.fiberhome.mapps.meetingroom.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fiberhome.mapps.meetingroom.entity.MrPrivilege;

public class ClassUtil
{
    public static <E> List<String> getUserIdList(List<E> list)
    {
        List<String> reList = new ArrayList<String>();
        for (Object obj : list)
        {
            if (obj instanceof MrPrivilege)
            {
                MrPrivilege mp = (MrPrivilege) obj;
                reList.add(mp.getEntityId());
            }
        }
        return reList;
    }

    public static <E> String getUserIds(List<E> list)
    {
        List<String> reList = getUserIdList(list);
        return StringUtils.join(reList.toArray(), ",");
    }
}
