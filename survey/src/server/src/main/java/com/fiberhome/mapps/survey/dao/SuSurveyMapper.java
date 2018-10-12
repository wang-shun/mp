package com.fiberhome.mapps.survey.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.survey.entity.SuSurvey;
import com.fiberhome.mapps.survey.entity.SuSurveyPojo;

public interface SuSurveyMapper extends MyMapper<SuSurvey>
{

    public List<SuSurveyPojo> getSurvey(Map<String, Object> map);

    public void updateSurveyPersons(@Param(value = "surveyId") String surveyId);

    public SuSurvey getSurveyByQuestion(@Param(value = "surveyId") String surveyId);

    public List<SuSurveyPojo> getSurveyForWeb(Map<String, Object> map);

    public void closeSurvey(Map<String, Object> map);

    public void publishSurvey(Map<String, Object> map);

    // v1.1
    public List<SuSurveyPojo> getMySurvey(Map<String, Object> map);
}