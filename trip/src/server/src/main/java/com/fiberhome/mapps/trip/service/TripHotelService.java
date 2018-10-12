package com.fiberhome.mapps.trip.service;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.trip.entity.CorplInfo;
import com.fiberhome.mapps.trip.entity.HotelBrand;
import com.fiberhome.mapps.trip.entity.HotelDetail;
import com.fiberhome.mapps.trip.entity.HotelGroup;
import com.fiberhome.mapps.trip.entity.HotelImages;
import com.fiberhome.mapps.trip.entity.HotelInfo;
import com.fiberhome.mapps.trip.entity.RoomStatusWithType;
import com.fiberhome.mapps.trip.entity.RoomType;
import com.fiberhome.mapps.trip.entity.HotelWithRmStatus;
import com.fiberhome.mapps.trip.entity.RoomStatus;
import com.fiberhome.mapps.trip.request.HotelDetailRequest;
import com.fiberhome.mapps.trip.request.HotelInfoRequest;
import com.fiberhome.mapps.trip.request.HotelRoomStatusRequest;
import com.fiberhome.mapps.trip.request.HotelWithRmStatusRequest;
import com.fiberhome.mapps.trip.request.RoomStatusWithTypeRequest;
import com.fiberhome.mapps.trip.response.CorpInfoResponse;
import com.fiberhome.mapps.trip.response.HotelBrandResponse;
import com.fiberhome.mapps.trip.response.HotelDetailResponse;
import com.fiberhome.mapps.trip.response.HotelGroupResponse;
import com.fiberhome.mapps.trip.response.HotelIdsResponse;
import com.fiberhome.mapps.trip.response.HotelImagesResponse;
import com.fiberhome.mapps.trip.response.HotelInfoResponse;
import com.fiberhome.mapps.trip.response.HotelRoomStatusResponse;
import com.fiberhome.mapps.trip.response.HotelRoomTypeResponse;
import com.fiberhome.mapps.trip.response.HotelWithRmStatusResponse;
import com.fiberhome.mapps.trip.response.RoomStatusWithTypeResponse;
import com.fiberhome.mapps.trip.utils.BtbtripClient;
import com.fiberhome.mapps.trip.utils.TripRequestUtil;
import com.rop.AbstractRopRequest;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean(version = "1.0")
public class TripHotelService {

	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	/**
	 * 1.2查询企业详情
	 * @param request 无参
	 * @return
	 * @throws JsonProcessingException
	 */
	@ServiceMethod(method = "mapps.trip.corpinfo", group = "hotel", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public CorpInfoResponse getCorpInfo(AbstractRopRequest request)
			throws JsonProcessingException {

		CorpInfoResponse res = new CorpInfoResponse();
		JSONObject json = BtbtripClient.sendRequest("/corp/getCorpInfo", null);

		if (json != null && "0".equals(json.getString("status"))) {
			// 成功回调
			JSONObject result = (JSONObject) json.get("data");
			res.setData(JSONObject.toJavaObject(result, CorplInfo.class));
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}
		return res;
	}
	
	/**
	 * 1.15查询酒店集团
	 * @param request 无参
	 * @return
	 * @throws JsonProcessingException
	 */
	@ServiceMethod(method = "mapps.trip.hotelgrouplist", group = "hotel", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public HotelGroupResponse getHotelGroupList(AbstractRopRequest request)
			throws JsonProcessingException {

		HotelGroupResponse res = new HotelGroupResponse();
		JSONObject json = BtbtripClient.sendRequest("/hotel/hotelGroupList", null);

		if (json != null && "0".equals(json.getString("status"))) {
			// 成功回调
			JSONArray result = (JSONArray) json.get("data");
			res.setData(JSONArray.parseArray(result.toString(), HotelGroup.class));
			
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}
		return res;
	}
	
	/**
	 * 1.16查询酒店品牌
	 * @param request 无参
	 * @return
	 * @throws JsonProcessingException
	 */
	@ServiceMethod(method = "mapps.trip.hotelbrand", group = "hotel", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public HotelBrandResponse getHotelBrand(AbstractRopRequest request)
			throws JsonProcessingException {

		HotelBrandResponse res = new HotelBrandResponse();
		JSONObject json = BtbtripClient.sendRequest("/hotel/selectHotelBrand", null);

		if (json != null && "0".equals(json.getString("status"))) {
			// 成功回调
			JSONArray result = (JSONArray) json.get("data");
			res.setData(JSONArray.parseArray(result.toString(), HotelBrand.class));
			
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}
		return res;
	}
	
	/**
	 * 1.17酒店列表（返回酒店ID列表）
	 * @param request  无参
	 * @return
	 * @throws JsonProcessingException
	 */
	@ServiceMethod(method = "mapps.trip.queryhotelids", group = "hotel", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public HotelIdsResponse queryHotelIds(AbstractRopRequest request)
			throws JsonProcessingException {

		HotelIdsResponse res = new HotelIdsResponse();
		JSONObject json = BtbtripClient.sendRequest("/hotel/queryHotelIds", null);

		if (json != null && "0".equals(json.getString("status"))) {
			// 成功回调
			JSONArray result = (JSONArray) json.get("data");
			res.setData(JSONArray.parseArray(result.toString(), String.class));
			
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}
		return res;
	}
	
	/**
	 * 1.18酒店基本信息详情
	 * @param hotelId 酒店编码
	 * @return
	 * @throws JsonProcessingException
	 */
	@ServiceMethod(method = "mapps.trip.gethotelinfo", group = "hotel", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public HotelInfoResponse getHotelInfo(HotelInfoRequest request)
			throws JsonProcessingException {
		
		//处理查询条件
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("hotelID", request.getHotelID());
//		map= TripRequstUtil.transRopRequest2Map(request) ;
		
		HotelInfoResponse res = new HotelInfoResponse();
//		JSONObject json = BtbtripClient.sendRequest("/hotel/getHotelInfo", map);
		JSONObject json = BtbtripClient.sendRequest("/hotel/getHotelInfo", TripRequestUtil.transRopRequest2Map(request));

		if (json != null && "0".equals(json.getString("status"))) {
			// 成功回调
			JSONObject result = (JSONObject) json.get("data");
			res.setData(JSONObject.parseObject(result.toString(), HotelInfo.class));
			
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}
		return res;
	}
	
	
	/**
	 * 1.19酒店相册 *
	 * @param hotelId 酒店编码
	 * @return
	 * @throws JsonProcessingException
	 */
	@ServiceMethod(method = "mapps.trip.hotelimages", group = "hotel", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public HotelImagesResponse queryHotelImages(HotelInfoRequest request)
			throws JsonProcessingException {
		
		//处理查询条件
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("hotelID", request.getHotelID());

		HotelImagesResponse res = new HotelImagesResponse();
		JSONObject json = BtbtripClient.sendRequest("/hotel/queryHotelImages", TripRequestUtil.transRopRequest2Map(request));

		if (json != null && "0".equals(json.getString("status"))) {
			// 成功回调
			JSONArray result = (JSONArray) json.get("data");
			res.setData(JSONArray.parseArray(result.toString(), HotelImages.class));
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}
		return res;
	}
	
	/**
	 * 1.20酒店房型
	 * @param hotelId 酒店编码
	 * @return
	 * @throws JsonProcessingException
	 */
	@ServiceMethod(method = "mapps.trip.hotelroomtype", group = "hotel", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public HotelRoomTypeResponse getHotelRoomType(HotelInfoRequest request)
			throws JsonProcessingException {
		
		//处理查询条件
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("hotelID", request.getHotelID());

		HotelRoomTypeResponse res = new HotelRoomTypeResponse();
		JSONObject json = BtbtripClient.sendRequest("/hotel/getHotelRoomType", TripRequestUtil.transRopRequest2Map(request));

		if (json != null && "0".equals(json.getString("status"))) {
			// 成功回调
			JSONArray result = (JSONArray) json.get("data");
			res.setData(JSONArray.parseArray(result.toString(), RoomType.class));
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}
		return res;
	}
	
	/**
	 * 1.21酒店房态搜索
	 * @param hotelID 酒店编码 beginDate 起始日期  endDate 结束日期  roomTypeID房型
	 * @return
	 * @throws JsonProcessingException
	 */
	@ServiceMethod(method = "mapps.trip.roomstatus", group = "hotel", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public HotelRoomStatusResponse queryRoomStatus(HotelRoomStatusRequest request)
			throws JsonProcessingException {
		
		//处理查询条件
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("hotelID", request.getHotelID());
//		map.put("beginDate", request.getBeginDate());
//		map.put("endDate", request.getEndDate());
//		if(request.getRoomTypeID() != null ){
//			map.put("roomTypeID", request.getRoomTypeID());
//		}
		
		HotelRoomStatusResponse res = new HotelRoomStatusResponse();
		JSONObject json = BtbtripClient.sendRequest("/hotel/queryRoomStatus", TripRequestUtil.transRopRequest2Map(request));

		if (json != null && "0".equals(json.getString("status"))) {
			// 成功回调
			JSONArray result = (JSONArray) json.get("data");
			res.setData(JSONArray.parseArray(result.toString(), RoomStatus.class));
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}
		return res;
	}
	
	
	
	/**
	 * 1.22酒店搜索（返回酒店信息列表、第一天房态、房型）
	 * @param HotelWithRmStatusRequest
	 * @return
	 * @throws JsonProcessingException
	 */
	@ServiceMethod(method = "mapps.trip.queryhotels", group = "hotel", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public HotelWithRmStatusResponse queryHotelsWithRmStatus(HotelWithRmStatusRequest request)
			throws JsonProcessingException {
		
		HotelWithRmStatusResponse res = new HotelWithRmStatusResponse();
		JSONObject json = BtbtripClient.sendRequest("/hotel/queryHotelsWithRmStatus", TripRequestUtil.transRopRequest2Map(request));

		//屏蔽东呈4
		Integer[] hotelGroup = new Integer[]{1,2,3};
		request.setHotelGroup(hotelGroup);
		
		if (json != null && "0".equals(json.getString("status"))) {
			// 成功回调
			JSONObject result = (JSONObject) json.get("data");
			
			res.setData(JSONObject.parseObject(result.toString(), HotelWithRmStatus.class));
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}
		return res;
	}
	
	/**
	 * 1.23酒店详情（返回酒店信息详情、第一天房态、房型）
	 * @param HotelDetailRequest
	 * @return
	 * @throws JsonProcessingException
	 */
	@ServiceMethod(method = "mapps.trip.hoteldetail", group = "hotel", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public HotelDetailResponse findHotelWithRmStatus(HotelDetailRequest request)
			throws JsonProcessingException {
		
		HotelDetailResponse res = new HotelDetailResponse();
		JSONObject json = BtbtripClient.sendRequest("/hotel/findHotelWithRmStatus", TripRequestUtil.transRopRequest2Map(request));

		if (json != null && "0".equals(json.getString("status"))) {
			// 成功回调
			JSONObject result = (JSONObject) json.get("data");
			res.setData(JSONObject.parseObject(result.toString(), HotelDetail.class));
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}
		return res;
	}
	
	/**
	 * 1.24查询酒店房态（包含房型信息）
	 * @param RoomStatusWithTypeRequest
	 * @return
	 * @throws JsonProcessingException
	 */
	@ServiceMethod(method = "mapps.trip.roomstatuswithrmtype", group = "hotel", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public RoomStatusWithTypeResponse queryRmStatusWithRmType(RoomStatusWithTypeRequest request)
			throws JsonProcessingException {
		
		RoomStatusWithTypeResponse res = new RoomStatusWithTypeResponse();
		JSONObject json = BtbtripClient.sendRequest("/hotel/queryRmStatusWithRmType", TripRequestUtil.transRopRequest2Map(request));

		if (json != null && "0".equals(json.getString("status"))) {
			// 成功回调
			JSONObject result = (JSONObject) json.get("data");
			res.setData(JSONObject.parseObject(result.toString(), RoomStatusWithType.class));
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}
		return res;
	}


}
