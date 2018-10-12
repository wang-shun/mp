package com.fiberhome.mapps.trip.entity;

import java.util.List;

public class TripOrderPage {
	
	private List<TripOrder> orders;
	
	private Integer PageIndex;// 第几页
	
	private Integer PageSize;// 每页最大记录数
	
	private Integer RecordCount;// 总共记录数
	
	private Integer PageCount;// 最大页码

	public List<TripOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<TripOrder> orders) {
		this.orders = orders;
	}

	public Integer getPageIndex() {
		return PageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		PageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return PageSize;
	}

	public void setPageSize(Integer pageSize) {
		PageSize = pageSize;
	}

	public Integer getRecordCount() {
		return RecordCount;
	}

	public void setRecordCount(Integer recordCount) {
		RecordCount = recordCount;
	}

	public Integer getPageCount() {
		return PageCount;
	}

	public void setPageCount(Integer pageCount) {
		PageCount = pageCount;
	}
	
}
