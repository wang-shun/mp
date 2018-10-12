package com.fiberhome.mapps.survey.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.survey.entity.SuAnswer;
import com.fiberhome.mapps.survey.entity.SurveyAnswer;

public interface SuAnswerMapper extends MyMapper<SuAnswer>
{

    public SuAnswer getTextAnswerByQuestion(Map<String, Object> map);

    public void addAnswer(SurveyAnswer entity);

    public List<SuAnswer> getExportData(Map<String, Object> map);
}