package com.fiberhome.mapps.trip.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.trip.entity.FdFeedback;

public interface FdFeedbackMapper extends MyMapper<FdFeedback>
{

    public List<FdFeedback> getFdFeedback(Map<String, Object> map);
}