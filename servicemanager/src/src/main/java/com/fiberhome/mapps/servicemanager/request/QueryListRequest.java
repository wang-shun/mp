package com.fiberhome.mapps.servicemanager.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class QueryListRequest extends AbstractRopRequest {
	@NotNull
    @Min(value = 1, message = "分页页数最小为1")
    private int offset;
	
    @NotNull
    @Min(value = 1, message = "每页记录数最小为1")
    @Max(value = 1000, message = "每页记录数最大为100")
    private int limit;
    
    private String sort;
    
    private String keyword;
    
    private String isenabled; //过滤条件:是否启用
    
    public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getIsenabled() {
		return isenabled;
	}

	public void setIsenabled(String isenabled) {
		this.isenabled = isenabled;
	}
}
