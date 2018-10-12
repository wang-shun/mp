package com.fiberhome.mapps.trip.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.validation.constraints.Pattern;

import com.rop.AbstractRopRequest;

public class MyOrderListRequestBt  extends AbstractRopRequest
{
	private Integer orderStatus;// 订单状态（1待付款，2本地取消，3待酒店确认，4预定成功，5预定失败，100酒店已取消，101noshow，102在住，103离店，104已评论）
	private Integer corpID;// 公司编号
	private Integer staffID;// 员工编号
	private Integer hotelGroupID;// 集团编号
	private Integer hotelID;// 酒店编号

	@Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$", message = "日期格式不合法,请使用【yyyy-MM-dd】")
	private String arrivalStart;// 实际入住时间范围时间戳

	@Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$", message = "日期格式不合法,请使用【yyyy-MM-dd】")
	private String arrivalEnd;// 实际入住时间范围时间戳
	private String orderCode;// 订单编码
	private String contactName;// 联系人姓名
	private String contactPhone;// 联系人手机
	private String guestName;// 入住人姓名
	private Integer payType;// 支付方式：1.储值支付 2到付 3支付宝 4微信
	private Integer orderType;// 订单类型（1公差，2私差）
	private Integer page;// 第几页，默认1
	private Integer rows;// 每页记录数，默认20

	
	//web参数
	private Integer offset;
	private Integer limit;

	private Long checkInTime ;
	private Long checkOutTime ;
	
	public Long getCheckInTime()
	{
		return this.getArrivalStart();
	}

	public void setCheckInTime(Long checkInTime)
	{
		this.checkInTime = checkInTime;
	}

	public Long getCheckOutTime()
	{
		return this.getArrivalEnd();
	}

	public void setCheckOutTime(Long checkOutTime)
	{
		this.checkOutTime = checkOutTime;
	}

	public Integer getOffset()
	{
		return offset;
	}

	public Integer getLimit()
	{
		return limit;
	}

	public void setOffset(Integer offset)
	{
		this.page = offset ;
//		this.offset = offset;
	}

	public void setLimit(Integer limit)
	{
		this.rows = limit ;
//		this.limit = limit;
	}
	
	public Integer getOrderStatus()
	{
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus)
	{
		this.orderStatus = orderStatus;
	}

	public Integer getCorpID()
	{
		return corpID;
	}

	public void setCorpID(Integer corpID)
	{
		this.corpID = corpID;
	}

	public Integer getStaffID()
	{
		return staffID;
	}

	public void setStaffID(Integer staffID)
	{
		this.staffID = staffID;
	}

	public Integer getHotelGroupID()
	{
		return hotelGroupID;
	}

	public void setHotelGroupID(Integer hotelGroupID)
	{
		this.hotelGroupID = hotelGroupID;
	}

	public Integer getHotelID()
	{
		return hotelID;
	}

	public void setHotelID(Integer hotelID)
	{
		this.hotelID = hotelID;
	}

	public Long getArrivalStart()
	{
		if(arrivalStart == null){
			return null ;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(arrivalStart).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0l ;
		}
	}

	public Long getArrivalEnd()
	{
		if(arrivalEnd == null){
			return null ;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(arrivalEnd).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0l ;
		}
	}

	
	public void setArrivalStart(String arrivalStart)
	{
		this.arrivalStart = arrivalStart;
	}

	public void setArrivalEnd(String arrivalEnd)
	{
		this.arrivalEnd = arrivalEnd;
	}

	public String getOrderCode()
	{
		return orderCode;
	}

	public void setOrderCode(String orderCode)
	{
		this.orderCode = orderCode;
	}

	public String getContactName()
	{
		return contactName;
	}

	public void setContactName(String contactName)
	{
		this.contactName = contactName;
	}

	public String getContactPhone()
	{
		return contactPhone;
	}

	public void setContactPhone(String contactPhone)
	{
		this.contactPhone = contactPhone;
	}

	public String getGuestName()
	{
		return guestName;
	}

	public void setGuestName(String guestName)
	{
		this.guestName = guestName;
	}

	public Integer getPayType()
	{
		return payType;
	}

	public void setPayType(Integer payType)
	{
		this.payType = payType;
	}

	public Integer getOrderType()
	{
		return orderType;
	}

	public void setOrderType(Integer orderType)
	{
		this.orderType = orderType;
	}

	public Integer getPage()
	{
		return page;
	}

	public void setPage(Integer page)
	{
		this.page = page;
	}

	public Integer getRows()
	{
		return rows;
	}

	public void setRows(Integer rows)
	{
		this.rows = rows;
	}

}
