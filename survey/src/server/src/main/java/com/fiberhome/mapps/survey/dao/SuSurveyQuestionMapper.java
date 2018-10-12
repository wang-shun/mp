package com.fiberhome.mapps.survey.dao;

import org.apache.ibatis.annotations.Param;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.survey.entity.SuSurveyQuestion;

public interface SuSurveyQuestionMapper extends MyMapper<SuSurveyQuestion>
{

    public void deleteSurveyQuestionBySurveyId(@Param(value = "surveyId") String surveyId);
}