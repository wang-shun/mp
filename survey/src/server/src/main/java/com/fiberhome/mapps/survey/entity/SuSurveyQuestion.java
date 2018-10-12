package com.fiberhome.mapps.survey.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "su_survey_question")
public class SuSurveyQuestion
{
    @Id
    @Column(name = "survey_id")
    private String surveyId;

    @Id
    @Column(name = "question_id")
    private String questionId;

    private Long   sequ;

    /**
     * @return survey_id
     */
    public String getSurveyId()
    {
        return surveyId;
    }

    /**
     * @param surveyId
     */
    public void setSurveyId(String surveyId)
    {
        this.surveyId = surveyId;
    }

    /**
     * @return question_id
     */
    public String getQuestionId()
    {
        return questionId;
    }

    /**
     * @param questionId
     */
    public void setQuestionId(String questionId)
    {
        this.questionId = questionId;
    }

    /**
     * @return sequ
     */
    public Long getSequ()
    {
        return sequ;
    }

    /**
     * @param sequ
     */
    public void setSequ(Long sequ)
    {
        this.sequ = sequ;
    }
}