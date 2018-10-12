package com.fiberhome.mapps.survey.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.survey.entity.SuAnswerPeople;
import com.fiberhome.mapps.survey.entity.SuAnswerPeoplePojo;

public interface SuAnswerPeopleMapper extends MyMapper<SuAnswerPeople>
{

    public SuAnswerPeople getAnswerPeopleByPerson(Map<String, Object> map);

    public SuAnswerPeople getAnswerPeopleByPersonQues(Map<String, Object> map);

    public void updateAnswered(Map<String, Object> map);

    public List<SuAnswerPeoplePojo> getAnswerPeopleList(Map<String, Object> map);

    public List<String> getAnalysisAnswer(String sql);

    public void updateNoticeStatusBySurveyPersonId(Map<String, Object> map);

}