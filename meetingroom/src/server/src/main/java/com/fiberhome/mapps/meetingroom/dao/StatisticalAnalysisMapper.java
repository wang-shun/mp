package com.fiberhome.mapps.meetingroom.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meetingroom.entity.StatisticalAnalysis;

public interface StatisticalAnalysisMapper extends MyMapper<StatisticalAnalysis>
{
    public List<StatisticalAnalysis> getStatisticalAnalysis(Map<String, Object> map);
}