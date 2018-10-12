package com.fiberhome.mapps.survey.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class FeedbackIdRequest extends AbstractRopRequest
{
    @NotNull
    private String feedbackId;

    private String problem;

    private String solution;

    private String confirm;

    public String getProblem()
    {
        return problem;
    }

    public void setProblem(String problem)
    {
        this.problem = problem;
    }

    public String getSolution()
    {
        return solution;
    }

    public void setSolution(String solution)
    {
        this.solution = solution;
    }

    public String getConfirm()
    {
        return confirm;
    }

    public void setConfirm(String confirm)
    {
        this.confirm = confirm;
    }

    public String getFeedbackId()
    {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId)
    {
        this.feedbackId = feedbackId;
    }

}
