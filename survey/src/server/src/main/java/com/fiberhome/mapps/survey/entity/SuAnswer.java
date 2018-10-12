package com.fiberhome.mapps.survey.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "su_answer")
public class SuAnswer
{
    @Id
    private String id;

    @Column(name = "question_id")
    private String questionId;

    private String name;
    private String nameHtml;

    @Column(name = "answer_id")
    private String answerId;

    private String answer;

    private Long   sequ;

    public String getNameHtml()
    {
        return nameHtml;
    }

    public void setNameHtml(String nameHtml)
    {
        this.nameHtml = nameHtml;
    }

    /**
     * @return id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id)
    {
        this.id = id;
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
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return answer_id
     */
    public String getAnswerId()
    {
        return answerId;
    }

    /**
     * @param answerId
     */
    public void setAnswerId(String answerId)
    {
        this.answerId = answerId;
    }

    /**
     * @return answer
     */
    public String getAnswer()
    {
        return answer;
    }

    /**
     * @param answer
     */
    public void setAnswer(String answer)
    {
        this.answer = answer;
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