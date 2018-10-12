package com.fiberhome.mapps.trip.request;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.rop.AbstractRopRequest;

public class MyOrderListRequest extends AbstractRopRequest {

	@NotNull
	private Integer rows;// 每页最大记录数，默认20
	
	@NotNull
	private Integer page;// 第几页，默认1

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

}
