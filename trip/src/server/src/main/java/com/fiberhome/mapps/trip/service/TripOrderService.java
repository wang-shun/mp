package com.fiberhome.mapps.trip.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.catalina.manager.util.SessionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.trip.dao.FdFeedbackMapper;
import com.fiberhome.mapps.trip.dao.TripOrderMapper;
import com.fiberhome.mapps.trip.entity.CorplInfo;
import com.fiberhome.mapps.trip.entity.HotelOrderDetail;
import com.fiberhome.mapps.trip.entity.HotelWithRmStatus;
import com.fiberhome.mapps.trip.entity.RoomStatusWithType;
import com.fiberhome.mapps.trip.entity.TripOrder;
import com.fiberhome.mapps.trip.entity.TripOrderPage;
import com.fiberhome.mapps.trip.entity.TripOrderPageBt;
import com.fiberhome.mapps.trip.request.FeedbackIdRequest;
import com.fiberhome.mapps.trip.request.MyOrderListRequest;
import com.fiberhome.mapps.trip.request.MyOrderListRequestBt;
import com.fiberhome.mapps.trip.request.OrderAddRequest;
import com.fiberhome.mapps.trip.request.OrderDetailRequest;
import com.fiberhome.mapps.trip.request.TripRequest;
import com.fiberhome.mapps.trip.response.FeedbackResponse;
import com.fiberhome.mapps.trip.response.OrderAddResponse;
import com.fiberhome.mapps.trip.response.OrderDetailBtResponse;
import com.fiberhome.mapps.trip.response.OrderDetailResponse;
import com.fiberhome.mapps.trip.response.OrderListBtResponse;
import com.fiberhome.mapps.trip.response.OrderListResponse;
import com.fiberhome.mapps.trip.response.RoomStatusWithTypeResponse;
import com.fiberhome.mapps.trip.response.TripResponse;
import com.fiberhome.mapps.trip.utils.BtbtripClient;
import com.fiberhome.mapps.trip.utils.TripRequestUtil;
import com.fiberhome.mapps.utils.IDGen;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean(version = "1.0")
public class TripOrderService {

	@Autowired
	private TripOrderMapper tripOrderMapper;
	@Autowired
	private TripUserService tripUserService ;

	/**
	 * 订单提交
	 * @param request
	 * @return
	 */
	@ServiceMethod(method = "mapps.trip.orderadd", group = "order", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public OrderAddResponse addOrder(OrderAddRequest request){
		
		OrderAddResponse res = new OrderAddResponse();
		//加载 铂旅员工号
		Integer staffId = tripUserService.getUserStaffId() ;
		if(staffId == null || staffId <= 0 ){
			res.setCode("0");
			if(staffId == -1){
				res.setError_message("账户未设置手机号，请设置手机号码后重试！");
			}else{				
				res.setError_message("获取员工信息失败，请重试！");
			}
			
			return res ;
		}
		
		
		//保存本地订单
		String id = IDGen.uuid();
//        String userName = SessionContext.getUserName();
		
		TripOrder order = new TripOrder();
		order.setOrderId(id);
		order.setUserId(SessionContext.getUserId());
		order.setOrgId(SessionContext.getOrgId());
		
		order.setBeginDate(request.getCheckintime());
		order.setEndDate(request.getCheckouttime());
		order.setUserName(request.getGuestsname());
		order.setLinkTel(request.getContactphone());
		order.setHotelName(request.getHotelid().toString());//酒店名称
		order.setRoomNumber(request.getRoomcount());
		order.setRoomType(request.getRoomtypeid().toString());
		order.setPrice(new BigDecimal(request.getPrice()));
		order.setCreateDate(new Date());
		order.setHasBreakfast(request.getHasBreakfast().toString());
		order.setHotelName(request.getHotelname());
		order.setHotelAddress(request.getHoteladdress());
		order.setHotelImg(request.getHotelimg());
		order.setHotelTel(request.getHoteltel());
		
		order.setHotelId(request.getHotelid().toString());
		order.setOrderStatus("1"); //待确认
		order.setPayType("2");//到付
		
        request.setStaffid(staffId);//默认员工编号  需要改成动态获取
		request.setOuterordercode(id);
		request.setPaymenttype(2);//到店支付
		
//		double amount = request.getPrice() * request.getRoomcount()* request.getTotalnight() ;
//		request.setTotalamount(amount);//总价计算
		//request.setTotalamount(374d);
		
		order.setTotalAmount(new BigDecimal(request.getTotalamount()));
		tripOrderMapper.insert(order) ;
		
		
		JSONObject json = BtbtripClient.sendRequest("/order/add", TripRequestUtil.transRopRequest2Map(request));

		if (json != null && "0".equals(json.getString("status"))) {
			res.setData((String) json.get("data"));
			//保存成功 更新订单ID
			order.setOutOrderId((String) json.get("data"));
			tripOrderMapper.updateByPrimaryKey(order);
			
		} else {
			TripRequestUtil.setErrorInfo(res,json);
			//发生错误删除记录
			tripOrderMapper.deleteByPrimaryKey(order) ;
		}
		
		return res;
	}
	

	/**
	 * 个人订单列表
	 * @param request
	 * @return
	 */
	@ServiceMethod(method = "mapps.trip.orderlist", group = "order", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public OrderListResponse getOrderList(MyOrderListRequest request){

		OrderListResponse res = new OrderListResponse();
		
		String userId = SessionContext.getUserId() ;
		//获取当前登录人所下的订单
		TripOrder order = new TripOrder() ;
		order.setUserId("123");
		
//		List<TripOrder> list = tripOrderMapper.select(order);
//		List<TripOrder> list =  tripOrderMapper.selectAll();
		
		int offset = (request.getPage()-1) * request.getRows() ;
//		RowBounds rowBounds = new RowBounds(offset,request.getRows());
//		List<TripOrder> list =   tripOrderMapper.selectByRowBounds(order, rowBounds) ;


		PageHelper.startPage(offset, request.getRows());
		PageHelper.orderBy("create_date desc");
		List<TripOrder> list =   tripOrderMapper.select(order) ;
        PageInfo<TripOrder> page = new PageInfo<TripOrder>(list);
		
        page.setTotal(list.size());
		
		TripOrderPage rt = new TripOrderPage() ;
		rt.setOrders(page.getList());
		rt.setPageIndex(request.getPage());
		rt.setPageSize(request.getRows());
		rt.setPageCount(1);
	
		res.setData(rt);
		
		return res;
	}
	
	/**
	 * 订单明细
	 * @param request
	 * @return
	 */
	@ServiceMethod(method = "mapps.trip.orderdetail", group = "order", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public OrderDetailResponse getOrderDetail(OrderDetailRequest request) {
		OrderDetailResponse res = new OrderDetailResponse();

		TripOrder order = tripOrderMapper.selectByPrimaryKey(request.getOrderId());
		res.setData(order);

		return res;
	}

	/**
	 * 订单取消
	 * @param request
	 * @return
	 */
	@ServiceMethod(method = "mapps.trip.ordercancel", group = "order", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public TripResponse orderCancel(OrderDetailRequest request) {
		TripResponse res = new TripResponse();

//		TripOrder order = tripOrderMapper.selectByPrimaryKey(request.getOrderId());
//		
//		request.setOrderId(order.getOutOrderId());
		JSONObject json = BtbtripClient.sendRequest("/order/cancel", TripRequestUtil.transRopRequest2Map(request));

		if (json != null && "0".equals(json.getString("status"))) {
			res.setData(json.get("data"));
			
//			TripOrder order = new TripOrder() ;
//			order.setOutOrderId(request.getOrderId());
//			List<TripOrder> orders = tripOrderMapper.selectByExample(order);
//			if(orders != null && orders.size()>0){
//				order = orders.get(0);
//				order.setOrderStatus("2");
//				order.setCancalDate(new Date());
//				order.setOutOrderId(request.getOrderId());
//				tripOrderMapper.updateByPrimaryKeySelective(order);
//			}
		
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}

		return res;
	}
	
	/**
	 * 订单提交状态查询
	 * @param request
	 * @return
	 */
	@ServiceMethod(method = "mapps.trip.checkbookingresult", group = "order", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public TripResponse orderCheckResult(OrderDetailRequest request) {
		TripResponse res = new TripResponse();

		TripOrder order = tripOrderMapper.selectByPrimaryKey(request.getOrderId());
		
		request.setOrderId(order.getOutOrderId());
		JSONObject json = BtbtripClient.sendRequest("/order/checkBookingPostResult", TripRequestUtil.transRopRequest2Map(request));

		if (json != null && "0".equals(json.getString("status"))) {
			res.setData(json.get("data"));
			
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}

		return res;
	}
	
	/**
	 * 订单取消状态查询
	 * @param request
	 * @return
	 */
	@ServiceMethod(method = "mapps.trip.checkcancelresult", group = "order", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public TripResponse orderCancelCheckResult(OrderDetailRequest request) {
		TripResponse res = new TripResponse();

		TripOrder order = tripOrderMapper.selectByPrimaryKey(request.getOrderId());
		
		request.setOrderId(order.getOutOrderId());
		JSONObject json = BtbtripClient.sendRequest("/order/checkCancelPostResult", TripRequestUtil.transRopRequest2Map(request));

		if (json != null && "0".equals(json.getString("status"))) {
			res.setData(json.get("data"));
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}

		return res;
	}
	
	/**
	 * 订单查询 - 铂旅
	 * @param request
	 * @return
	 */
	@ServiceMethod(method = "mapps.trip.orderlist.bt", group = "order", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public OrderListBtResponse getOrderList(MyOrderListRequestBt request) {
		
		OrderListBtResponse res = new OrderListBtResponse();
		
		
		request.setStaffID(tripUserService.getUserStaffId());

		JSONObject json = BtbtripClient.sendRequest("/order/qryPages", TripRequestUtil.transRopRequest2Map(request));

		if (json != null && "0".equals(json.getString("status"))) {
			// 成功回调
			JSONObject result = (JSONObject) json.get("data");
			
			res.setData(JSONObject.parseObject(result.toString(), TripOrderPageBt.class));
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}

		return res;
	}
	
	/**
	 * 订单明细 - 铂旅
	 * @param request
	 * @return
	 */
	@ServiceMethod(method = "mapps.trip.orderdetail.bt", group = "order", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public OrderDetailBtResponse orderDetail(OrderDetailRequest request) {
		
		OrderDetailBtResponse res = new OrderDetailBtResponse();
		
		JSONObject json = BtbtripClient.sendRequest("/order/getDetail", TripRequestUtil.transRopRequest2Map(request));

		if (json != null && "0".equals(json.getString("status"))) {
			// 成功回调
			JSONObject result = (JSONObject) json.get("data");
			
			res.setData(JSONObject.parseObject(result.toString(), HotelOrderDetail.class));
		} else {
			TripRequestUtil.setErrorInfo(res,json);
		}

		return res;
	}
	
	//for web 

	/**
	 * 订单查询 - 铂旅    web端查询
	 * @param request
	 * @return
	 */
	@ServiceMethod(method = "mapps.trip.orderlistall.bt", group = "order", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public TripOrderPageBt orderList(MyOrderListRequestBt request) {
		
		TripOrderPageBt res = new TripOrderPageBt();
		CorplInfo corp = null;
		//查询机构ID
		JSONObject json = BtbtripClient.sendRequest("/corp/getCorpInfo", null);

		if (json != null && "0".equals(json.getString("status"))) {
			// 成功回调
			JSONObject result = (JSONObject) json.get("data");
			corp = JSONObject.toJavaObject(result, CorplInfo.class);
		}
		if(corp != null){
			request.setCorpID(corp.getCorpid());//(tripUserService.getUserStaffId());
			//非admin用户查自己
			if(!SessionContext.isAdmin()){
				request.setStaffID(tripUserService.getUserStaffId());
			}
			
			JSONObject json2 = BtbtripClient.sendRequest("/order/qryPages", TripRequestUtil.transRopRequest2Map(request));

			if (json2 != null && "0".equals(json2.getString("status"))) {
				// 成功回调
				JSONObject result = (JSONObject) json2.get("data");
				
				res = JSONObject.parseObject(result.toString(), TripOrderPageBt.class);
			} else {
				TripRequestUtil.setErrorInfo(res,json2);
			}
		}else{
			res.setError_message("获取企业信息失败");
			res.setCode("0");
		}
		

		return res;
	}
	
}
