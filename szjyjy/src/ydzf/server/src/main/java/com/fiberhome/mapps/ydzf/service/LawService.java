package com.fiberhome.mapps.ydzf.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.contact.service.MplusAccessService;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.ydzf.dao.LawImFoodMapper;
import com.fiberhome.mapps.ydzf.entity.ContainerDetail;
import com.fiberhome.mapps.ydzf.entity.GoodsDetail;
import com.fiberhome.mapps.ydzf.entity.LawExpFoodCosmetics;
import com.fiberhome.mapps.ydzf.entity.LawFile;
import com.fiberhome.mapps.ydzf.entity.LawFoodCosmetics;
import com.fiberhome.mapps.ydzf.entity.LawImFoodCosmetics;
import com.fiberhome.mapps.ydzf.entity.LawSampling;
import com.fiberhome.mapps.ydzf.entity.LawSamplingList;
import com.fiberhome.mapps.ydzf.request.QueryLawDetailRequest;
import com.fiberhome.mapps.ydzf.request.QueryLawListRequest;
import com.fiberhome.mapps.ydzf.response.QueryGoodsDetailResponse;
import com.fiberhome.mapps.ydzf.response.QueryLawDetailResponse;
import com.fiberhome.mapps.ydzf.response.QueryLawListResponse;
import com.fiberhome.mapps.ydzf.utils.DateUtil;
import com.fiberhome.mapps.ydzf.utils.ErrorCode;
import com.fiberhome.mapps.ydzf.utils.HttpsUtils;
import com.fiberhome.mapps.ydzf.utils.JsonUtil;
import com.fiberhome.mapps.ydzf.utils.LogUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean
public class LawService {
	protected final Logger           LOGGER            = LoggerFactory.getLogger(getClass());
	
	@Autowired
    MplusAccessService     accessService;
	
	@Autowired
	LawImFoodMapper    lawImFoodMapper;
	@Value("${api.goodsdetail}")
    private String goodsdetailUrl;
    @Value("${api.containerdetail}")
    public String containerdetailUrl;
    
    @Value("${services.fileservice.imagePath}")
    private String imagePath;
    /**
     * ("法制处","风险管理处","通关业务处","质量安全监督管理处","卫生检疫处","动植物检疫监管处","食品检验监督处","信息中心")的部门Id
     */
    private static ArrayList<String> DeptIds = new ArrayList<String>(Arrays.asList("d9ff4c62a586818f0d5a13e954a00226","4c26d08671755ac2dbd68da6ce1ff32c","c80fd2001b5acb7e93c4857ac1e9a0f8",
    		"c8a000f3424e1b4477c3408ad7db4df4","b31f9973fc540d3e4bcdb860564eefbb","2805ef9e9541db52197a1f6f0f8bd045",
    		"d5f0cf7506a783be5f3f07f1b0361466","2cab3e19455d864d5c019fce5cf36165"));

	/**
	 * 获取分页列表数据
	 * @param req
	 * @return
	 */
    @ServiceMethod(method = "mapps.ydzf.query.list", group = "member", groupTitle = "查询列表", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryLawListResponse querLawList(QueryLawListRequest req){
        LOGGER.info("查询列表接口(mapps.ydzf.query.list)入口,请求参数==" + LogUtil.getObjectInfo(req));
    	QueryLawListResponse res = new QueryLawListResponse();
    	try {
    		Map<String, Object> map = new HashMap<String, Object>();
        	this.initMap(map, req);
        	if("2".equals(req.getStatus())){
        		map.put("inspectorCode", SessionContext.getUserId());
        	}else if("1".equals(req.getStatus())){
        		
        		map.put("userId", SessionContext.getUserId());
        	}else{
        		if(!isExist(SessionContext.getOrgId(),SessionContext.getUserId())){
        			List<String> userIds = new ArrayList<String>();
            		userIds = this.getUserIds(SessionContext.getDeptId(),SessionContext.getOrgId());
            		map.put("userIds",userIds);
        		}
        	}
        	
        	PageHelper.startPage(req.getOffset(), req.getLimit());
        	List<LawFoodCosmetics> list=  lawImFoodMapper.queryLawlList(map);
        	PageInfo<LawFoodCosmetics> page = new PageInfo<LawFoodCosmetics>(list);
            res.setTotal(page.getTotal());
            res.setLawList(list);
            res.setTimestamp(req.getTimestamp());
            if (page.isIsLastPage())
            {
                res.setEndflag(1);
            }
            else
            {
                res.setEndflag(0);
            }
            res.success();
		} catch (Exception e) {
			e.printStackTrace();
            LOGGER.error("分页查询信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
		}
    	
    	
    	
		return res;
    }
    /**
     * 查询条件初始化
     * @param map
     * @param req
     */
    public void initMap(Map<String,Object> map,QueryLawListRequest req){
    	if(!StringUtils.isEmpty(req.getStartTime())){
    		try {
				Date startTime = DateUtil.sdf.parse(req.getStartTime());
				map.put("startTime", startTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	if(!StringUtils.isEmpty(req.getEndTime())){
    		try {
				Date endTime = DateUtil.sdf.parse(req.getEndTime());
				Calendar   calendar   =   new   GregorianCalendar(); 
			     calendar.setTime(endTime); 
			     calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
			     endTime=calendar.getTime();   //这个时间就是日期往后推一天的结果 
				map.put("endTime", endTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}
    	if(!StringUtils.isEmpty(req.getName())){
    		map.put("name", req.getName());
    	}
    	if(!StringUtils.isEmpty(req.getDeclNo())){
    		map.put("declNo", req.getDeclNo());
    	}
    	if(!StringUtils.isEmpty(req.getSort())){
    		String sort = "";
    		if(req.getSort().contains("confmTimeStr")){
    			sort = req.getSort().replace("Str", "");
    		}else{
    			sort = req.getSort();
    		}
    		map.put("sort", sort);
    	}
    }
    /**
	 * 获取详情
	 * @param req
	 * @return
	 */   
    @ServiceMethod(method = "mapps.ydzf.query.detail", group = "member", groupTitle = "详情", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryLawDetailResponse queryLawDetail(QueryLawDetailRequest req){
        LOGGER.info("详情接口(mapps.ydzf.query.detail)入口,请求参数==" + LogUtil.getObjectInfo(req));
    	QueryLawDetailResponse res = new QueryLawDetailResponse();
    	try {
    		LawImFoodCosmetics law = new LawImFoodCosmetics();
    		LawExpFoodCosmetics lawExp = new LawExpFoodCosmetics();
        	law.setDeclNo(req.getDeclNo());
        	Map<String,Object> map = new HashMap<String,Object>();
        	map.put("declNo", req.getDeclNo());
        	if("1".equals(req.getDeclNo().substring(0, 1))){
        		law = lawImFoodMapper.selectOne(law);
        		res.setType("1");
        	}else{
        		lawExp = lawImFoodMapper.getLawOutDetail(map);
        		res.setType("2");
        	};
        	List<LawFile> lawFiles =  lawImFoodMapper.getLawFile(map);
        	for (LawFile lawFile : lawFiles) {
        		lawFile.setFilePath(imagePath+lawFile.getFilePath());
			}
        	List<LawSampling> lawSamplings = new ArrayList<LawSampling>();
        	lawSamplings = lawImFoodMapper.getLawSamlpings(map);
        	for (LawSampling lawSampling : lawSamplings) {
        		Map<String,Object> mapLaw = new HashMap<String,Object>();
        		mapLaw.put("declNo", req.getDeclNo());
        		mapLaw.put("samplingId", lawSampling.getSamplingId());
        		List<LawSamplingList> lawSamplingLists= lawImFoodMapper.getLawSamplingLists(mapLaw);
        		lawSampling.setLawSamplingLists(lawSamplingLists);
			}
        	res.setLawExpFoodCosmetics(lawExp);
        	res.setLawFoodCosmetics(law);
        	res.setLawSamplings(lawSamplings);
        	res.setLawFiles(lawFiles);
        	res.success();
		} catch (Exception e) {
			e.printStackTrace();
            LOGGER.error("详情获取异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
		}
    	
    	return res;
    }
    
    
    @ServiceMethod(method = "mapps.ydzf.query.goodsdetail", group = "member", groupTitle = "详情", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryGoodsDetailResponse queryGoodsDetail(QueryLawDetailRequest req){
    	LOGGER.info("货物详情接口(mapps.ydzf.query.goodsdetail)入口,请求参数==" + LogUtil.getObjectInfo(req));
    	QueryGoodsDetailResponse res = new QueryGoodsDetailResponse();
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("decl_no",req.getDeclNo());
    	try {
    		LOGGER.info("请求货物信息接口http://gjet.szciqic.net:8001/process/service/mlaw/goodsdetail.do,请求参数==" + LogUtil.getObjectInfo(req));
    		String str = HttpsUtils.sendGet(goodsdetailUrl, map);
    		LOGGER.info("请求货物信息result" + str);
        	JSONObject goodsDetailJson = JSONObject.parseObject(str);
        	List<GoodsDetail> goodsDetails = new ArrayList<GoodsDetail>();
        	if(goodsDetailJson!=null){
            	goodsDetails= (List<GoodsDetail>) JsonUtil.jsonToObject(goodsDetailJson.getString("resJson"), GoodsDetail.class);
        	}
        	LOGGER.info("请求集装箱信息接口http://gjet.szciqic.net:8001/process/service/mlaw/containerdetail.do,请求参数==" + LogUtil.getObjectInfo(req));
    		String containerdetailStr = HttpsUtils.sendGet(containerdetailUrl, map);
    		LOGGER.info("请求集装箱信息result" + str);
        	JSONObject containerdetailJson = JSONObject.parseObject(containerdetailStr);
        	List<ContainerDetail> containerDetails= new ArrayList<ContainerDetail>();
        	if(containerdetailJson!=null){
            	containerDetails= (List<ContainerDetail>) JsonUtil.jsonToObject(containerdetailJson.getString("resJson"), GoodsDetail.class);

        	}
        	res.setGoodsDetails(goodsDetails);
        	res.setContainerDetails(containerDetails);
        	res.success();
		
    	} catch (Exception e) {
			e.printStackTrace();
            LOGGER.error("详情获取异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
		}
    	
    	return res;
    }
    
    
    
    /**
     * 获取所有userId集合
     */
    public List<String> getUserIds(String deptId, String orgId)
    {
        List<String> userIds = new ArrayList<>();
       
        try
        {
            List<MyUser> userInfo = accessService.getUsersByDept(orgId, deptId, "1", 0);
            if (userInfo.size() > 0)
            {
                for (MyUser user : userInfo)
                {
                    userIds.add(user.getLoginId());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.debug("获取部门人员失败 ");
        }
        
        return userIds;
    }
    /**
     * 判断当前登录人在不在  ("法制处","风险管理处","通关业务处","质量安全监督管理处","卫生检疫处","动植物检疫监管处","食品检验监督处","信息中心");

     */
    public boolean isExist(String orgId,String loginId){
    	List<MyUser> userInfos = new ArrayList<MyUser>();
    	List<String> userIds = new ArrayList<>();
    	for (String deptId : DeptIds) {
    		 try {
				List<MyUser> userInfo = accessService.getUsersByDept(orgId, deptId, "1", 0);
				if(userInfo!=null&&userInfo.size()>0){
					userInfos.addAll(userInfo);
				}
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
    	if (userInfos.size() > 0)
        {
            for (MyUser user : userInfos)
            {
                userIds.add(user.getLoginId());
            }
        }
    	
    	return userIds.contains(loginId);
    }
    
}
