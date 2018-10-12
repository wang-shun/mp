package com.fiberhome.mapps.trip.request;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.rop.AbstractRopRequest;

public class OrderAddRequest extends AbstractRopRequest {
	
	private String outerordercode;// 是 外部关联编号
	
	private Integer staffid;// 是 员工编号
	
	@NotNull
	private Integer hotelid;// 是 酒店编号
	
	@NotNull
	private Integer roomtypeid;// 是 房型编号
	
	@NotNull
	private String contactname;// 是 联系人姓名
	
	@NotNull
	private String contactphone;// 是 联系人电话
	
	@NotNull
	private String guestsname;// 是 入住人姓名
	
	@NotNull
	private Integer roomcount;// 是 预定房间数
	
	@NotNull
	@Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$", message = "日期格式不合法,请使用【yyyy-MM-dd】")
	private String checkintime;// 是 入住时间 2017-01-01
	
	@NotNull
	@Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$", message = "日期格式不合法,请使用【yyyy-MM-dd】")
	private String checkouttime;// 是 离店时间 2017-01-01

	private Integer paymenttype;// 是 支付方式：1.储值支付 2到付 3支付宝 4微信
	
	private String remark;// 备注
	
	private Integer travelApplyId;// 申请编号
	@NotNull
	private Integer hasBreakfast = 0;// 是否含早（1含），默认0
	@NotNull
	private Double totalamount ;
	
	//以下 非铂旅接口字段
	
	private Double price ;
	
	private String hotelname ;
	
	private String hoteltel ;
	
	private String hoteladdress ;
	
	private String hotellocation ;
	
	private String hotelimg ; 
	
	private Integer totalnight = 1  ;
	
	public Integer getTotalnight() {
		return totalnight;
	}

	public void setTotalnight(Integer totalnight) {
		this.totalnight = totalnight;
	}

	public String getHotelname() {
		return hotelname;
	}

	public void setHotelname(String hotelname) {
		this.hotelname = hotelname;
	}

	public String getHoteltel() {
		return hoteltel;
	}

	public void setHoteltel(String hoteltel) {
		this.hoteltel = hoteltel;
	}

	public String getHoteladdress() {
		return hoteladdress;
	}

	public void setHoteladdress(String hoteladdress) {
		this.hoteladdress = hoteladdress;
	}

	public String getHotellocation() {
		return hotellocation;
	}

	public void setHotellocation(String hotellocation) {
		this.hotellocation = hotellocation;
	}

	public String getHotelimg() {
		return hotelimg;
	}

	public void setHotelimg(String hotelimg) {
		this.hotelimg = hotelimg;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(Double totalamount) {
		this.totalamount = totalamount;
	}

	public String getOuterordercode() {
		return outerordercode;
	}

	public void setOuterordercode(String outerordercode) {
		this.outerordercode = outerordercode;
	}

	public Integer getStaffid() {
		return staffid;
	}

	public void setStaffid(Integer staffid) {
		this.staffid = staffid;
	}

	public Integer getHotelid() {
		return hotelid;
	}

	public void setHotelid(Integer hotelid) {
		this.hotelid = hotelid;
	}

	public Integer getRoomtypeid() {
		return roomtypeid;
	}

	public void setRoomtypeid(Integer roomtypeid) {
		this.roomtypeid = roomtypeid;
	}

	public String getContactname() {
		return contactname;
	}

	public void setContactname(String contactname) {
		this.contactname = contactname;
	}

	public String getContactphone() {
		return contactphone;
	}

	public void setContactphone(String contactphone) {
		this.contactphone = contactphone;
	}

	public String getGuestsname() {
		return guestsname;
	}

	public void setGuestsname(String guestsname) {
		this.guestsname = guestsname;
	}

	public Integer getRoomcount() {
		return roomcount;
	}

	public void setRoomcount(Integer roomcount) {
		this.roomcount = roomcount;
	}

	public Integer getPaymenttype() {
		return paymenttype;
	}

	public void setPaymenttype(Integer paymenttype) {
		this.paymenttype = paymenttype;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getTravelApplyId() {
		return travelApplyId;
	}

	public void setTravelApplyId(Integer travelApplyId) {
		this.travelApplyId = travelApplyId;
	}

	public Integer getHasBreakfast() {
		return hasBreakfast;
	}

	public void setHasBreakfast(Integer hasBreakfast) {
		this.hasBreakfast = hasBreakfast;
	}

	public String getCheckintime() {
		return checkintime ;
	}
	

	public String getCheckouttime() {
		return checkouttime ;
	}
	
//	public Long getCheckintime() {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			return sdf.parse(checkintime).getTime();
//		} catch (ParseException e) {
//			e.printStackTrace();
//			return 0l ;
//		}
//	}
	

//	public Long getCheckouttime() {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			return sdf.parse(checkouttime).getTime();
//		} catch (ParseException e) {
//			e.printStackTrace();
//			return 0l ;
//		}
//	}
	
	
	public void setCheckintime(String checkintime) {
		this.checkintime = checkintime;
	}

	public void setCheckouttime(String checkouttime) {
		this.checkouttime = checkouttime;
	}

	
}
