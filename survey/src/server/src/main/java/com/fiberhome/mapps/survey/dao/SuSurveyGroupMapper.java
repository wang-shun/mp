package com.fiberhome.mapps.survey.dao;

import org.apache.ibatis.annotations.Param;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.survey.entity.SuSurveyGroup;

public interface SuSurveyGroupMapper extends MyMapper<SuSurveyGroup>
{

    public Long getMaxSequ(@Param(value = "ecid") String ecid);

    public void updateGroupName(SuSurveyGroup group);
}