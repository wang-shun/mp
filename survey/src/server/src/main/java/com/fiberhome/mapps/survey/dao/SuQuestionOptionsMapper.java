package com.fiberhome.mapps.survey.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.survey.entity.AnalysisQuestionOptionsJson;
import com.fiberhome.mapps.survey.entity.SuQuestionOptions;
import com.fiberhome.mapps.survey.entity.SuQuestionOptionsJson;

public interface SuQuestionOptionsMapper extends MyMapper<SuQuestionOptions>
{

    public List<SuQuestionOptions> getQuestionOptionsByQuestionId(@Param(value = "questionId") String questionId);

    public List<SuQuestionOptionsJson> getOptionsAnswersByQuestionId(Map<String, Object> map);

    public void deleteQuestionOptionBySurveyId(@Param(value = "surveyId") String surveyId);

    public void deleteQuestionOptionByTemplateId(@Param(value = "templateId") String templateId);

    public List<AnalysisQuestionOptionsJson> getQuestionAnalysis(Map<String, Object> map);

    public long getQuestionAnswerCount(Map<String, Object> map);

    public String getOptionByQuestionAndSequ(Map<String, Object> map);
}