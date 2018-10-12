package com.fiberhome.mapps.ydzf.request;

import com.rop.AbstractRopRequest;

public class QueryLawDetailRequest extends AbstractRopRequest{
	private String declNo;

	public String getDeclNo() {
		return declNo;
	}

	public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}
}
