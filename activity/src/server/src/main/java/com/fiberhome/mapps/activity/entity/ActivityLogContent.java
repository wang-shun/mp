package com.fiberhome.mapps.activity.entity;

public class ActivityLogContent extends BaseOpLogContent{
	private String actId;
	
    private String actContent;
    
    public ActivityLogContent(String actId ,String actContent, String message){
    	this.setMessage(message);
    	this.actId = actId;
    	this.actContent = actContent;
    }
    
    public String toString(){
    	return "actId=" +actId+ ",actContent="+actContent+",message="+this.getMessage();
    }

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getActContent() {
		return actContent;
	}

	public void setActContent(String actContent) {
		this.actContent = actContent;
	}


    
}
