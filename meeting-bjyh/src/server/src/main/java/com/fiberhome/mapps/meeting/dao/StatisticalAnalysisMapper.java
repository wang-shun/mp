package com.fiberhome.mapps.meeting.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.meeting.entity.StatisticalAnalysis;

public interface StatisticalAnalysisMapper extends MyMapper<StatisticalAnalysis>
{
    public List<StatisticalAnalysis> getStatisticalAnalysis(Map<String, Object> map);
}