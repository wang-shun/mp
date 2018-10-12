package com.fiberhome.mapps.survey.entity;

import java.util.List;

public class SurveyContent {

	private String pager;
	
	private String submitTime;
	
	private String isAnswer;
	
	private String status;
	
	private List<QuestionContent> question;

	public String getPager() {
		return pager;
	}

	public void setPager(String pager) {
		this.pager = pager;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getIsAnswer() {
		return isAnswer;
	}

	public void setIsAnswer(String isAnswer) {
		this.isAnswer = isAnswer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<QuestionContent> getQuestion() {
		return question;
	}

	public void setQuestion(List<QuestionContent> question) {
		this.question = question;
	}
	
	
}
