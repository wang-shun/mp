package com.fiberhome.mapps.trip.entity;

import java.math.BigDecimal;
import java.util.List;

public class HotelInfo {
	private 	Integer	Level	;//		星级(1：经济型,2：舒适型，3：品质型，4：高档型，5：豪华型)
	private 	Integer	FavouCount	;//		好评数
	private 	BigDecimal	FavouRate	;//		好评率
	private 	Integer	Commit	;//		评论数
	private 	Integer	FaciStar	;//		设施评分
	private 	Integer	ServiceStar	;//		服务评分
	private 	Integer	HygiStar	;//		卫生评分
	private 	Integer	ConveStar	;//		地理位置（方便性）评分
	private 	Integer	HolisticScore	;//		综合评分
	private 	BigDecimal	Mlng	;//		火星坐标-经度
	private 	BigDecimal	Mlat	;//		火星坐标-纬度
	private 	BigDecimal	Lat	;//		地球坐标-纬度
	private 	BigDecimal	Lng	;//		地球坐标-经度
	private 	String	ChkinTips	;//		入住提醒（温馨提示）
	private 	List<ExtendInfo>	Service	;//		酒店服务
	private 	List<ExtendInfo>	Facility	;//		酒店设施
	private 	List<ExtendInfo>	RmFaci	;//		房间设施
	private 	List<ExtendInfo>	RecrFaci	;//		娱乐设施
	private 	String	Desc	;//		酒店介绍
	private 	List<String>	Traffic	;//		交通信息
	private 	String	HotelName	;//		酒店全名
	private 	String	ShortName	;//		酒店简称
	private 	String	BrandName	;//		品牌名称
	private 	Integer	Brand	;//		品牌ID
	private 	String	Tel	;//		电话
	private 	Integer	HotelID	;//		酒店编号
	private 	Integer	HotelGroup	;//		集团编号（1：铂涛，2：华住，3：维也纳，4：东呈）
	private 	String	PcityCode	;//		国家级城市编码
	private 	String	CityCode	;//		城市/城镇编码
	private 	String	DistCode	;//		行政区域
	private 	List<String>	Keywords	;//		关键字
	private 	String	Addr	;//		地址
	private 	String	ImgUrl	;//		主图（小）
	public Integer getLevel() {
		return Level;
	}
	public void setLevel(Integer level) {
		Level = level;
	}
	public Integer getFavouCount() {
		return FavouCount;
	}
	public void setFavouCount(Integer favouCount) {
		FavouCount = favouCount;
	}
	public BigDecimal getFavouRate() {
		return FavouRate;
	}
	public void setFavouRate(BigDecimal favouRate) {
		FavouRate = favouRate;
	}
	public Integer getCommit() {
		return Commit;
	}
	public void setCommit(Integer commit) {
		Commit = commit;
	}
	public Integer getFaciStar() {
		return FaciStar;
	}
	public void setFaciStar(Integer faciStar) {
		FaciStar = faciStar;
	}
	public Integer getServiceStar() {
		return ServiceStar;
	}
	public void setServiceStar(Integer serviceStar) {
		ServiceStar = serviceStar;
	}
	public Integer getHygiStar() {
		return HygiStar;
	}
	public void setHygiStar(Integer hygiStar) {
		HygiStar = hygiStar;
	}
	public Integer getConveStar() {
		return ConveStar;
	}
	public void setConveStar(Integer conveStar) {
		ConveStar = conveStar;
	}
	public Integer getHolisticScore() {
		return HolisticScore;
	}
	public void setHolisticScore(Integer holisticScore) {
		HolisticScore = holisticScore;
	}
	public BigDecimal getMlng() {
		return Mlng;
	}
	public void setMlng(BigDecimal mlng) {
		Mlng = mlng;
	}
	public BigDecimal getMlat() {
		return Mlat;
	}
	public void setMlat(BigDecimal mlat) {
		Mlat = mlat;
	}
	public BigDecimal getLat() {
		return Lat;
	}
	public void setLat(BigDecimal lat) {
		Lat = lat;
	}
	public BigDecimal getLng() {
		return Lng;
	}
	public void setLng(BigDecimal lng) {
		Lng = lng;
	}
	public String getChkinTips() {
		return ChkinTips;
	}
	public void setChkinTips(String chkinTips) {
		ChkinTips = chkinTips;
	}
	public List<ExtendInfo> getService() {
		return Service;
	}
	public void setService(List<ExtendInfo> service) {
		Service = service;
	}
	public List<ExtendInfo> getFacility() {
		return Facility;
	}
	public void setFacility(List<ExtendInfo> facility) {
		Facility = facility;
	}
	public List<ExtendInfo> getRmFaci() {
		return RmFaci;
	}
	public void setRmFaci(List<ExtendInfo> rmFaci) {
		RmFaci = rmFaci;
	}
	public List<ExtendInfo> getRecrFaci() {
		return RecrFaci;
	}
	public void setRecrFaci(List<ExtendInfo> recrFaci) {
		RecrFaci = recrFaci;
	}
	public String getDesc() {
		return Desc;
	}
	public void setDesc(String desc) {
		Desc = desc;
	}
	public List<String> getTraffic() {
		return Traffic;
	}
	public void setTraffic(List<String> traffic) {
		Traffic = traffic;
	}
	public String getHotelName() {
		return HotelName;
	}
	public void setHotelName(String hotelName) {
		HotelName = hotelName;
	}
	public String getShortName() {
		return ShortName;
	}
	public void setShortName(String shortName) {
		ShortName = shortName;
	}
	public String getBrandName() {
		return BrandName;
	}
	public void setBrandName(String brandName) {
		BrandName = brandName;
	}
	public Integer getBrand() {
		return Brand;
	}
	public void setBrand(Integer brand) {
		Brand = brand;
	}
	public String getTel() {
		return Tel;
	}
	public void setTel(String tel) {
		Tel = tel;
	}
	public Integer getHotelID() {
		return HotelID;
	}
	public void setHotelID(Integer hotelID) {
		HotelID = hotelID;
	}
	public Integer getHotelGroup() {
		return HotelGroup;
	}
	public void setHotelGroup(Integer hotelGroup) {
		HotelGroup = hotelGroup;
	}
	public String getPcityCode() {
		return PcityCode;
	}
	public void setPcityCode(String pcityCode) {
		PcityCode = pcityCode;
	}
	public String getCityCode() {
		return CityCode;
	}
	public void setCityCode(String cityCode) {
		CityCode = cityCode;
	}
	public String getDistCode() {
		return DistCode;
	}
	public void setDistCode(String distCode) {
		DistCode = distCode;
	}
	public List<String> getKeywords() {
		return Keywords;
	}
	public void setKeywords(List<String> keywords) {
		Keywords = keywords;
	}
	public String getAddr() {
		return Addr;
	}
	public void setAddr(String addr) {
		Addr = addr;
	}
	public String getImgUrl() {
		return ImgUrl;
	}
	public void setImgUrl(String imgUrl) {
		ImgUrl = imgUrl;
	}

}
