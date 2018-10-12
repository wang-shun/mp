package com.fiberhome.mapps.servicemanager.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.servicemanager.dao.SmRedisMapper;
import com.fiberhome.mapps.servicemanager.dao.SmResourceConfigMapper;
import com.fiberhome.mapps.servicemanager.dao.SmResourceInfoMapper;
import com.fiberhome.mapps.servicemanager.dao.SmResourceInfoitemMapper;
import com.fiberhome.mapps.servicemanager.dao.SmResourceMapper;
import com.fiberhome.mapps.servicemanager.entity.ClientResourceInfo;
import com.fiberhome.mapps.servicemanager.entity.RsKeyValue;
import com.fiberhome.mapps.servicemanager.entity.SmResource;
import com.fiberhome.mapps.servicemanager.entity.SmResourceConfig;
import com.fiberhome.mapps.servicemanager.entity.SmResourceInfo;
import com.fiberhome.mapps.servicemanager.entity.SmResourceInfoitem;
import com.fiberhome.mapps.servicemanager.request.ItemIdRequest;
import com.fiberhome.mapps.servicemanager.request.QueryListRequest;
import com.fiberhome.mapps.servicemanager.request.ResourceIdRequest;
import com.fiberhome.mapps.servicemanager.request.ResourceInfoSaveRequest;
import com.fiberhome.mapps.servicemanager.response.ResourceDetailResponse;
import com.fiberhome.mapps.servicemanager.response.ResourceInfoListResponse;
import com.fiberhome.mapps.servicemanager.response.ResourceTypeItemResponse;
import com.fiberhome.mapps.servicemanager.response.ResourceTypeResponse;
import com.fiberhome.mapps.servicemanager.utils.ErrorCode;
import com.fiberhome.mapps.servicemanager.utils.IDGen;
import com.fiberhome.mapps.servicemanager.utils.LogUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.rop.AbstractRopRequest;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean
public class ResourceManagerService {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	SmRedisMapper redisMapper;

	@Autowired
	SmResourceInfoMapper resourceInfoMapper;
	
	@Autowired
	SmResourceInfoitemMapper resourceInfoitemMapper;
	
	@Autowired
	SmResourceConfigMapper resourceConfigMapper;
	
	@Autowired
	SmResourceMapper resourceMapper;

	@ServiceMethod(method = "mapps.servicemanager.resource.info.list", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public ResourceInfoListResponse getRedisList(QueryListRequest req) {
		LOGGER.debug("===获取resouceInfo接口(mapps.servicemanager.resource.info.list)入口===");
		ResourceInfoListResponse response = new ResourceInfoListResponse();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if (req.getIsenabled() != null) {
				map.put("isenabled", req.getIsenabled());
			}
			if (req.getKeyword() != null) {
				map.put("keyword", "%" + req.getKeyword() + "%");
			}
			if (StringUtil.isNotEmpty(req.getSort())) {
				String sort = "";
				if (req.getSort().contains("resId")) {
					sort = req.getSort().replaceAll("resId", "res_id");
				} else {
					sort = req.getSort();
				}
				map.put("sort", sort);
			}
			PageHelper.startPage(req.getOffset(), req.getLimit());
			List<SmResource> list = resourceMapper.getResourceList(map);
			PageInfo<SmResource> page = new PageInfo<SmResource>(list);
			response.setTotal(page.getTotal());
			if (list == null) {
				list = new ArrayList<SmResource>();
			}
			List<ClientResourceInfo> finalList = new ArrayList<ClientResourceInfo>();
			for(SmResource rc : list){
				ClientResourceInfo cri = new ClientResourceInfo(rc);
				SmResourceInfo sri = resourceInfoMapper.selectByPrimaryKey(cri.getResId());
				cri.setResName(sri.getName());
				SmResourceConfig rcc = new SmResourceConfig();
				rcc.setResourceId(cri.getId());
				rcc.setActived("1");
				List<SmResourceConfig> resouceConfigList = resourceConfigMapper.select(rcc);
				Map<String,String> configList = new HashMap<String,String>();
				for(SmResourceConfig src : resouceConfigList){
					configList.put(src.getParamKey(), src.getParamValue());
				}
				cri.setConfigList(configList);
				finalList.add(cri);
			}
			response.setResourceList(finalList);
		} catch (Exception e) {
			LOGGER.error("获取资源详情异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.resource.info.detail", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public ResourceDetailResponse getDetail(ResourceIdRequest req) {
		LOGGER.debug("===获取资源详情接口(mapps.servicemanager.resource.info.detail)入口===");
		ResourceDetailResponse response = new ResourceDetailResponse();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", req.getResourceId());
			SmResource resourceDetail = resourceMapper.getResourceDetailById(map);
			response.setResourceDetail(resourceDetail);
			SmResourceConfig rcc = new SmResourceConfig();
			rcc.setResourceId(req.getResourceId());
			rcc.setActived("1");
			List<SmResourceConfig> resouceConfigList = resourceConfigMapper.select(rcc);
			List<RsKeyValue> configList = new ArrayList<RsKeyValue>();
			for(SmResourceConfig src : resouceConfigList){
				RsKeyValue rskv = new RsKeyValue();
				rskv.setKey(src.getParamKey());
				rskv.setValue(src.getParamValue());
				configList.add(rskv);
			}
			response.setConfigList(configList);
			LOGGER.debug("获取资源详情成功");
		} catch (Exception e) {
			LOGGER.error("获取资源详情异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.resource.info.add", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse addResourceInfo(ResourceInfoSaveRequest req) {
		LOGGER.debug("===新增资源信息接口(mapps.servicemanager.resource.info.add)入口,请求参数==" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			SmResource rsPre = new SmResource();
			rsPre.setName(req.getName());
			List<SmResource> rsPreList = resourceMapper.select(rsPre);
			if (rsPreList.size() > 0) {
				ErrorCode.fail(response, ErrorCode.CODE_300056);
				return response;
			} else {
				Date curtime = new Date();
				String resourceId = IDGen.uuid().replaceAll("-", "");
				JSONArray configList = JSONArray.parseArray(req.getConfigList());
				for(int i=0;i<configList.size();i++){
					JSONObject cf = configList.getJSONObject(i);
					SmResourceConfig rc = new SmResourceConfig();
					rc.setId(IDGen.uuid().replaceAll("-", ""));
					rc.setResourceId(resourceId);
					rc.setParamKey(cf.getString("key"));
					rc.setParamValue(cf.getString("value"));
					rc.setConfigVer((long)0);
					rc.setActived("1");
					rc.setSetupUser(SessionContext.getUserId());
					rc.setSetupTime(curtime);
					resourceConfigMapper.insertSelective(rc);
				}
				SmResourceInfo ri = resourceInfoMapper.selectByPrimaryKey(req.getResId());
				SmResource rs = new SmResource();
				rs.setId(resourceId);
				rs.setResId(ri.getId());
				rs.setName(req.getName());
				rs.setRemarks(ri.getRemarks());
				rs.setCreator(SessionContext.getUserId());
				rs.setCreateTime(curtime);
				rs.setModifier(SessionContext.getUserId());
				rs.setModifyTime(curtime);
				rs.setEnabled("1");
				resourceMapper.insertSelective(rs);
				LOGGER.debug("新增资源信息成功");
			}
		} catch (Exception e) {
			LOGGER.error("新增资源信息异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.resource.info.edit", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse saveDetail(ResourceInfoSaveRequest req) {
		LOGGER.debug("===保存资源信息接口(mapps.servicemanager.resource.info.edit)入口,请求参数==" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			Date curtime = new Date();
			String resourceId = req.getId();
			JSONArray configList = JSONArray.parseArray(req.getConfigList());
			SmResourceConfig rcc = new SmResourceConfig();
			rcc.setResourceId(resourceId);
			rcc.setActived("1");
			List<SmResourceConfig> rcList = resourceConfigMapper.select(rcc);
			if(rcList.size()>0){
				for(SmResourceConfig src : rcList){
					src.setActived("0");
					resourceConfigMapper.updateByPrimaryKeySelective(src);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("resourceId", resourceId);
				long configVer = resourceConfigMapper.getConfigVersion(map);
				configVer++;
				for(int i=0;i<configList.size();i++){
					JSONObject cf = configList.getJSONObject(i);
					SmResourceConfig rc = new SmResourceConfig();
					rc.setId(IDGen.uuid().replaceAll("-", ""));
					rc.setResourceId(resourceId);
					rc.setParamKey(cf.getString("key"));
					rc.setParamValue(cf.getString("value"));
					rc.setConfigVer(configVer);
					rc.setActived("1");
					rc.setSetupUser(SessionContext.getUserId());
					rc.setSetupTime(curtime);
					resourceConfigMapper.insertSelective(rc);
				}
			}else{
				for(int i=0;i<configList.size();i++){
					JSONObject cf = configList.getJSONObject(i);
					SmResourceConfig rc = new SmResourceConfig();
					rc.setId(IDGen.uuid().replaceAll("-", ""));
					rc.setResourceId(resourceId);
					rc.setParamKey(cf.getString("key"));
					rc.setParamValue(cf.getString("value"));
					rc.setConfigVer((long)0);
					rc.setActived("1");
					rc.setSetupUser(SessionContext.getUserId());
					rc.setSetupTime(curtime);
					resourceConfigMapper.insertSelective(rc);
				}
			}
			SmResourceInfo ri = resourceInfoMapper.selectByPrimaryKey(req.getResId());
			SmResource rs = new SmResource();
			rs.setId(resourceId);
			rs.setResId(ri.getId());
			rs.setName(req.getName());
			rs.setRemarks(ri.getRemarks());
			rs.setModifier(SessionContext.getUserId());
			rs.setModifyTime(curtime);
			resourceMapper.updateByPrimaryKeySelective(rs);
			LOGGER.debug("保存资源信息成功");
		} catch (Exception e) {
			LOGGER.error("保存资源信息异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.resource.disable", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse stopRedis(ResourceIdRequest req) {
		LOGGER.debug("===禁用资源接口(mapps.servicemanager.resource.disable)===");
		BaseResponse response = new BaseResponse();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", req.getResourceId());
		try {
			resourceMapper.disableResourceById(map);
			LOGGER.debug("禁用资源成功");
		} catch (Exception e) {
			LOGGER.error("禁用资源异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.resource.enable", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse startRedis(ResourceIdRequest req) {
		LOGGER.debug("===启用资源接口(mapps.servicemanager.resource.enable)===");
		BaseResponse response = new BaseResponse();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", req.getResourceId());
		try {
			resourceMapper.enableResourceById(map);
			LOGGER.debug("启用资源成功");
		} catch (Exception e) {
			LOGGER.error("启用资源异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.resource.type.list", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public ResourceTypeResponse getResourceType(AbstractRopRequest req) {
		LOGGER.debug("===获取资源类型接口(mapps.servicemanager.resource.type.list)入口===");
		ResourceTypeResponse response = new ResourceTypeResponse();
		try {
			List<SmResourceInfo> resourceType = resourceInfoMapper.selectAll();
			response.setResourceType(resourceType);
			LOGGER.debug("获取资源类型成功");
		} catch (Exception e) {
			LOGGER.error("获取资源类型异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.resource.type.item", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public ResourceTypeItemResponse getResourceItem(ItemIdRequest req) {
		LOGGER.debug("===获取资源项接口(mapps.servicemanager.resource.type.item)入口,请求参数===" + LogUtil.getObjectInfo(req));
		ResourceTypeItemResponse response = new ResourceTypeItemResponse();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("infoId", req.getInfoId());
			List<SmResourceInfoitem> resourceTypeItemList = resourceInfoitemMapper.getTypeItem(map);
			response.setResourceTypeItemList(resourceTypeItemList);
			LOGGER.debug("获取资源项成功");
		} catch (Exception e) {
			LOGGER.error("获取资源项异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
}
