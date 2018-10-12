package com.fiberhome.mapps.survey.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "su_common_question")
public class SuCommonQuestion
{
    @Id
    private String id;

    @Column(name = "question_id")
    private String questionId;

    private String ecid;

    @Column(name = "tag_name")
    private String tagName;

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
     * @return ecid
     */
    public String getEcid()
    {
        return ecid;
    }

    /**
     * @param ecid
     */
    public void setEcid(String ecid)
    {
        this.ecid = ecid;
    }

    /**
     * @return tag_name
     */
    public String getTagName()
    {
        return tagName;
    }

    /**
     * @param tagName
     */
    public void setTagName(String tagName)
    {
        this.tagName = tagName;
    }
}