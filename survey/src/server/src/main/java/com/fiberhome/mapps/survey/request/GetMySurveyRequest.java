package com.fiberhome.mapps.survey.request;


import com.rop.AbstractRopRequest;

public class GetMySurveyRequest extends AbstractRopRequest
{
    private String title;
    
    private String mystatus;
	
	private int    offset;
	
    private int    limit;

    private String sort;
    private String order;

    public String getMystatus()
    {
        return mystatus;
    }

    public void setMystatus(String mystatus)
    {
        this.mystatus = mystatus;
    }

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }

    public String getTitle()
    {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
