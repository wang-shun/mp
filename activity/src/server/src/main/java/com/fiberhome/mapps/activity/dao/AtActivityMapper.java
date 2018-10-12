package com.fiberhome.mapps.activity.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.activity.entity.ActivityDetail;
import com.fiberhome.mapps.activity.entity.AtActivity;
import com.fiberhome.mapps.intergration.mybatis.MyMapper;

public interface AtActivityMapper extends MyMapper<AtActivity>
{

    public List<ActivityDetail> getActList(Map<String, Object> map);

    public List<ActivityDetail> getMyEnterActList(Map<String, Object> map);

    public List<ActivityDetail> getMyCreateActList(Map<String, Object> map);

    public List<ActivityDetail> getActivity(Map<String, Object> map);

    public void updateActivityByAddEnter(Map<String, Object> map);
}