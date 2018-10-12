package com.fiberhome.mapps.survey.dao;

import org.apache.ibatis.annotations.Param;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.survey.entity.SuTemplateQuestion;

public interface SuTemplateQuestionMapper extends MyMapper<SuTemplateQuestion>
{

    public void deleteTemplateQuestionBySurveyId(@Param(value = "surveyId") String surveyId);
}