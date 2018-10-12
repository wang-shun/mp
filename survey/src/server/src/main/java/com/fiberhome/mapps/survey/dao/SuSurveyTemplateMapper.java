package com.fiberhome.mapps.survey.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.survey.entity.SuSurveyTemplate;
import com.fiberhome.mapps.survey.entity.SuSurveyTemplatePojo;

public interface SuSurveyTemplateMapper extends MyMapper<SuSurveyTemplate>
{

    public List<SuSurveyTemplatePojo> getSurveyTemplate(Map<String, Object> map);

    public void updateUseTimes(@Param(value = "surveyId") String surveyId);
}