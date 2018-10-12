package com.fiberhome.mapps.trip.entity;

import java.util.List;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

public class TripOrderPageBt extends BaseResponse
{
	private Integer total;// 总记录数
	private Integer page;// 当前页
	private Integer pages;// 最大页数
	private Integer rows ;//当前页记录数
	private List<HotelOrder> list;// 数据集

	public Integer getRows()
	{
		return rows;
	}

	public void setRows(Integer rows)
	{
		this.rows = rows;
	}

	
	public Integer getTotal()
	{
		return total;
	}

	public void setTotal(Integer total)
	{
		this.total = total;
	}

	public Integer getPage()
	{
		return page;
	}

	public void setPage(Integer page)
	{
		this.page = page;
	}

	public Integer getPages()
	{
		return pages;
	}

	public void setPages(Integer pages)
	{
		this.pages = pages;
	}

	public List<HotelOrder> getList()
	{
		return list;
	}

	public void setList(List<HotelOrder> list)
	{
		this.list = list;
	}

}
