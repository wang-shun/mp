package com.fiberhome.mapps.trip.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.trip.dao.TripUserStaffidMapper;
import com.fiberhome.mapps.trip.entity.RoomStatusWithType;
import com.fiberhome.mapps.trip.entity.StaffAddMultiDTO;
import com.fiberhome.mapps.trip.entity.TripUserStaffid;
import com.fiberhome.mapps.trip.request.FeedbackIdRequest;
import com.fiberhome.mapps.trip.request.OrderAddRequest;
import com.fiberhome.mapps.trip.request.TripRequest;
import com.fiberhome.mapps.trip.response.FeedbackResponse;
import com.fiberhome.mapps.trip.response.OrderAddResponse;
import com.fiberhome.mapps.trip.response.RoomStatusWithTypeResponse;
import com.fiberhome.mapps.trip.response.TripUserResponse;
import com.fiberhome.mapps.trip.utils.BtbtripClient;
import com.fiberhome.mapps.trip.utils.TripRequestUtil;
import com.fiberhome.mapps.utils.IDGen;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;
@ServiceMethodBean(version = "1.0")
public class TripUserService {

	@Autowired
	private TripUserStaffidMapper tripUserStaffidMapper ;
	@Autowired
	private ThirdPartAccessService thirdPartAccessService ;
	
	
	@ServiceMethod(method = "mapps.trip.userinfo", group = "user", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public TripUserResponse getCurrentUser(TripRequest request){
		TripUserResponse res = new TripUserResponse();
		MyUser user = null ;
		try
		{
			user = thirdPartAccessService.getUserInfo(SessionContext.getOrgId(), SessionContext.getUserId());
			if(user != null){
				res.setData(user);
				res.setCode("1");
			}else{
				res.setError_message("获取mplus用户信息失败");
				res.setCode("0");
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			res.setError_message("连接mplus异常");
			res.setCode("0");
		}
		
		return res ;
	}
	
	
	@ServiceMethod(method = "mapps.trip.setadmin", group = "user", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public TripUserResponse setAdmin(TripRequest request){
		TripUserResponse res = new TripUserResponse();
		String staffId = request.getData().toString() ;
		Map<String,Object> listmap = new HashMap<String, Object>();
		listmap.clear(); 
		listmap.put("staffId", staffId);
		listmap.put("authType", 1);
		JSONObject json2 = BtbtripClient.sendRequest("staff/adminAuthEx", listmap);
		if(json2.getInteger("status") == 0){
			res.setCode("1");
		}else{
			res.setCode("0");
		}
		return res ;
	}
	
	
	@ServiceMethod(method = "mapps.trip.closeaudit", group = "user", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public TripUserResponse closeaudit(TripRequest request){
		TripUserResponse res = new TripUserResponse();
		Map<String,Object> listmap = new HashMap<String, Object>();
		listmap.put("flowType", 99);
		listmap.put("applyStrict", 2);
		JSONObject json2 = BtbtripClient.sendRequest("corp/updateApplyFlowTypeEx", listmap);
		if(json2.getInteger("status") == 0){
			res.setCode("1");
		}else{
			res.setCode("0");
		}
		return res ;
	}
	
	public Integer getUserStaffId() {
		
		String userId = "admin" ;
		userId = SessionContext.getUserId() ;
		
		TripUserStaffid staff = tripUserStaffidMapper.selectByPrimaryKey(userId);
		if(staff != null){
			return staff.getStaffId() ;
		}else{
			//创建用户
			MyUser user;
			try {
				
				user = thirdPartAccessService.getUserInfo(SessionContext.getOrgId(), SessionContext.getUserId());
				StaffAddMultiDTO vo = new StaffAddMultiDTO();
				
//				String phoneNum = "" ;
				if(user.getPhoneNum()==null || "".equals(user.getPhoneNum())){
					return -1 ;
				}
				
				vo.setMobile(user.getPhoneNum());
				vo.setRealName(user.getUserName());
				vo.setPayForOther(1);
				vo.setLevelId(0);
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>() ;
				Map<String,Object> listmap = new HashMap<String, Object>();
				list.add(TripRequestUtil.transRopRequest2Map(vo));
				listmap.put("staffList", list);
				JSONObject json = BtbtripClient.sendRequest("/staff/addMultiEx", listmap);
				
				if(json.getInteger("status") == 0){
					//成功回调
					Integer staffId = ((JSONObject)json.getJSONArray("data").get(0)).getInteger("staffId") ;
					
					staff = new TripUserStaffid() ;
					staff.setStaffId(staffId);
					staff.setUserId(userId);
					staff.setUserName(user.getUserName());
					staff.setCreateDate(new Date());
					tripUserStaffidMapper.insert(staff) ;
					
					return staffId ;
				}

//				return staffId ;
//				以下代码为设置管理员
//				listmap.clear(); 
//				listmap.put("staffId", staffId);
//				listmap.put("authType", 1);
//				JSONObject json2 = BtbtripClient.sendRequest("staff/adminAuthEx", listmap);
//				if(json2.getInteger("status") == 0){
//					//成功回调
//					staff = new TripUserStaffid() ;
//					staff.setStaffId(staffId);
//					staff.setUserId(userId);
//					staff.setUserName(user.getUserName());
//					staff.setCreateDate(new Date());
//					tripUserStaffidMapper.insert(staff) ;
//					
//					return staffId ;
//				}else{
//					return null ;
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null ;
	}
	
	
	
	public void addUser(String username ,String mobile){
		StaffAddMultiDTO vo = new StaffAddMultiDTO();
		vo.setMobile(mobile);
		vo.setRealName(username);
		vo.setPayForOther(1);
		vo.setLevelId(0);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>() ;
		Map<String,Object> listmap = new HashMap<String, Object>(); 
		list.add(TripRequestUtil.transRopRequest2Map(vo));
		listmap.put("staffList", list);
		JSONObject json = BtbtripClient.sendRequest("/staff/addMultiEx", listmap);
		System.out.println(json);
	}

	public void editUser(Integer staffId , String username ,String mobile){
		StaffAddMultiDTO vo = new StaffAddMultiDTO();
		vo.setPayForOther(1);
		vo.setStaffId(staffId);
		vo.setMobile(mobile);
		vo.setRealName(username);
		vo.setLevelId(0);
		JSONObject json = BtbtripClient.sendRequest("/staff/updateEx", TripRequestUtil.transRopRequest2Map(vo));
		System.out.println(json);
	}
	
	public void addAdmin(){

		Map<String,Object> listmap = new HashMap<String, Object>(); 
		listmap.put("staffId", 10000081);
		listmap.put("authType", 1);
		JSONObject json = BtbtripClient.sendRequest("staff/adminAuthEx", listmap);
		System.out.println(json);
	}
	
	
	public void deleteStaffId(Integer staffid){

		Map<String,Object> listmap = new HashMap<String, Object>(); 
		listmap.put("staffId", staffid);//10000080
		JSONObject json = BtbtripClient.sendRequest("staff/delStaff", listmap);
		System.out.println(json);
	}
	
	
	public static void main(String[] args) {
//		new TripUserService().editUser(10000081,"烽火","18151696335");
//		new TripUserService().deleteStaffId(10000080);
		
		
//		Map<String,Object> listmap = new HashMap<String, Object>(); 
//		listmap.put("flowType", 99);
//		listmap.put("applyStrict", 2);
//		JSONObject json = BtbtripClient.sendRequest("/corp/updateApplyFlowTypeEx", listmap);
//		System.out.println(json);
	}
	
}
