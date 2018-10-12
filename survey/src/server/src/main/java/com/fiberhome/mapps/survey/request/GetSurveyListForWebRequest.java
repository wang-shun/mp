package com.fiberhome.mapps.survey.request;


import com.rop.AbstractRopRequest;

public class GetSurveyListForWebRequest extends AbstractRopRequest
{
    private String title;
    
    private String status;
	
	private int    offset;
	
    private int    limit;
    
    private String sort;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
    
}
