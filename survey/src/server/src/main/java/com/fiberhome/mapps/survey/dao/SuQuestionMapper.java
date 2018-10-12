package com.fiberhome.mapps.survey.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.survey.entity.SuQuestion;
import com.fiberhome.mapps.survey.entity.SuQuestionJson;

public interface SuQuestionMapper extends MyMapper<SuQuestion>
{

    public List<SuQuestion> getQuestionBySurveyId(@Param(value = "surveyId") String surveyId);

    public List<SuQuestion> getQuestionByTemplateId(@Param(value = "templateId") String templateId);

    public void deleteSurveyQuestionBySurveyId(@Param(value = "surveyId") String surveyId);

    public void deleteTemplateQuestionBySurveyId(@Param(value = "surveyId") String surveyId);

    public List<SuQuestionJson> getSelectQuestion(@Param(value = "surveyId") String surveyId);

    public List<SuQuestionJson> getExportQuestion(@Param(value = "surveyId") String surveyId);
}