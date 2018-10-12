package com.fiberhome.mapps.trip.request;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.rop.AbstractRopRequest;

public class HotelWithRmStatusRequest extends AbstractRopRequest {
	
	private Integer[] hotelGroup;// 集团
	
	private Integer[] hotelID;// 酒店
	
	private Integer[] brand;// 品牌
	
	private Integer[] level;// 星级
	
	private String[] cityCode;// 城市
	
	private String[] districtCode;// 区域
	
	private String keywords;// 关键字
	
	@NotNull
	@Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$", message = "日期格式不合法,请使用【yyyy-MM-dd】")
	private String beginDate;// 入住日期 2017-01-01
	
	@NotNull
	@Pattern(regexp = "^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$", message = "日期格式不合法,请使用【yyyy-MM-dd】")
	private String endDate;// 离店日期 2017-01-01
	
	private BigDecimal price1;// 最小房晚单价价格
	
	private BigDecimal price2;// 最大房晚单价价格
	
	private String[] orderByField;// 排序 *+ (desc|asc)
	
	private Integer pageIndex;// 第几页，默认1
	
	private Integer pageSize;// 每页最大记录数，默认20

	private Integer page;// 第几页，默认1
	
	
	private Double lng ;//			经度
	private Double lat	; //		纬度
	
	private Double minDistance; //	最小距离
	
	private Double maxDistance; //	最大距离
	
	
	public Double getLng()
	{
		return lng;
	}

	public void setLng(Double lng)
	{
		this.lng = lng;
	}

	public Double getLat()
	{
		return lat;
	}

	public void setLat(Double lat)
	{
		this.lat = lat;
	}

	public Double getMinDistance()
	{
		return minDistance;
	}

	public void setMinDistance(Double minDistance)
	{
		this.minDistance = minDistance;
	}

	public Double getMaxDistance()
	{
		return maxDistance;
	}

	public void setMaxDistance(Double maxDistance)
	{
		this.maxDistance = maxDistance;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	private Integer rows;// 每页最大记录数，默认20
	
	
	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer[] getHotelGroup() {
		return hotelGroup;
	}

	public void setHotelGroup(Integer[] hotelGroup) {
		this.hotelGroup = hotelGroup;
	}

	public Integer[] getHotelID() {
		return hotelID;
	}

	public void setHotelID(Integer[] hotelID) {
		this.hotelID = hotelID;
	}

	public Integer[] getBrand() {
		return brand;
	}

	public void setBrand(Integer[] brand) {
		this.brand = brand;
	}

	public Integer[] getLevel() {
		return level;
	}

	public void setLevel(Integer[] level) {
		this.level = level;
	}

	public String[] getCityCode() {
		return cityCode;
	}

	public void setCityCode(String[] cityCode) {
		this.cityCode = cityCode;
	}

	public String[] getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String[] districtCode) {
		this.districtCode = districtCode;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

//	public String getBeginDate() {
//		return beginDate;
//	}

	public long getBeginDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(beginDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0l ;
		}
	}
	
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public Long getEndDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(endDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0l ;
		}
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getPrice1() {
		return price1;
	}

	public void setPrice1(BigDecimal price1) {
		this.price1 = price1;
	}

	public BigDecimal getPrice2() {
		return price2;
	}

	public void setPrice2(BigDecimal price2) {
		this.price2 = price2;
	}

	public String[] getOrderByField() {
		return orderByField;
	}

	public void setOrderByField(String[] orderByField) {
		this.orderByField = orderByField;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
