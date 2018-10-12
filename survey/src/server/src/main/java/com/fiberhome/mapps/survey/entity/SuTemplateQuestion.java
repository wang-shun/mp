package com.fiberhome.mapps.survey.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "su_template_question")
public class SuTemplateQuestion
{
    @Id
    @Column(name = "template_id")
    private String templateId;

    @Id
    @Column(name = "question_id")
    private String questionId;

    private Long   sequ;

    /**
     * @return template_id
     */
    public String getTemplateId()
    {
        return templateId;
    }

    /**
     * @param templateId
     */
    public void setTemplateId(String templateId)
    {
        this.templateId = templateId;
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