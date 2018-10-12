package com.fiberhome.mapps.servicemanager.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.transaction.annotation.Transactional;

import com.fiberhome.mapps.mssdk.RegInfo;
import com.fiberhome.mapps.redismq.pubsub.PubSub;
import com.fiberhome.mapps.servicemanager.dao.SmRouteMapper;
import com.fiberhome.mapps.servicemanager.entity.SmRoute;
import com.fiberhome.mapps.servicemanager.request.QueryRouteRequest;
import com.fiberhome.mapps.servicemanager.request.RouteAddExRequest;
import com.fiberhome.mapps.servicemanager.request.RouteAddRegRequest;
import com.fiberhome.mapps.servicemanager.request.RouteDetailRequest;
import com.fiberhome.mapps.servicemanager.request.RouteIdRequest;
import com.fiberhome.mapps.servicemanager.request.RouteSaveExRequest;
import com.fiberhome.mapps.servicemanager.request.RouteSaveRegRequest;
import com.fiberhome.mapps.servicemanager.request.ServiceIdRequest;
import com.fiberhome.mapps.servicemanager.response.RouteDetailResponse;
import com.fiberhome.mapps.servicemanager.response.RouteIdResponse;
import com.fiberhome.mapps.servicemanager.response.RouteListResponse;
import com.fiberhome.mapps.servicemanager.response.ServiceListResponse;
import com.fiberhome.mapps.servicemanager.response.ZuulRoute;
import com.fiberhome.mapps.servicemanager.utils.ErrorCode;
import com.fiberhome.mapps.servicemanager.utils.IDGen;
import com.fiberhome.mapps.servicemanager.utils.LogUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.rop.AbstractRopRequest;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean
public class RouteManagerService {
	public final String ROUTE_UPDATE_CHANNEL = "mapps.servicemanager.route.channel";
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	SmRouteMapper routeMapper;

	@Autowired
	ConfigManagerService cms;

	@Autowired
	DiscoveryClient discovery;

	@Autowired
	private EurekaClient discoveryClient;

	@Autowired
	private PubSub pubsub;

	@Value("${gateway.serviceUrl}")
	String gateWayServiceUrl;

	public void addRoute(SmRoute route) {
		routeMapper.insert(route);
	}

	public void removeRoute(String routeId) {
		routeMapper.deleteByPrimaryKey(routeId);
	}

	public void updateRoute(SmRoute route) {
		routeMapper.updateByPrimaryKey(route);
	}

	@ServiceMethod(method = "mapps.servicemanager.route.query", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public RouteListResponse queryRouteList(QueryRouteRequest req) {
		RouteListResponse response = new RouteListResponse();
		try {
			LOGGER.debug("===路由列表查询接口(mapps.servicemanager.route.query)入口,请求参数==" + LogUtil.getObjectInfo(req));

			Map<String, Object> map = new HashMap<String, Object>();
			if (req.getKeyword() != null) {
				map.put("keyword", "%" + req.getKeyword() + "%");
			}
			if (StringUtil.isNotEmpty(req.getSort())) {
				String sort = "";
				if (req.getSort().contains("serviceId")) {
					sort = req.getSort().replaceAll("serviceId", "service_id");
				} else if (req.getSort().contains("serviceName")) {
					sort = req.getSort().replaceAll("serviceName", "service_name");
				} else {
					sort = req.getSort();
				}
				map.put("sort", sort);
			}
			PageHelper.startPage(req.getOffset(), req.getLimit());
			List<SmRoute> list = routeMapper.getRouteList(map);
			PageInfo<SmRoute> page = new PageInfo<SmRoute>(list);
			response.setTotal(page.getTotal());
			if (list == null) {
				list = new ArrayList<SmRoute>();
			}
			List<ZuulRoute> result = new ArrayList<ZuulRoute>();
			for (SmRoute sr : list) {
				Set<String> sensitiveHeaders = new LinkedHashSet<>();
				String ignoreHeaders = sr.getSensitiveHeaders();
				if (StringUtils.isNotEmpty(ignoreHeaders)) {
					String[] headers = ignoreHeaders.split("[;]");
					for (String header : headers) {
						sensitiveHeaders.add(header);
					}
				}
				int serviceNodeNumber = 0;
				String portalLink = "";
				Applications apps = discoveryClient.getApplications();
				Application app = apps.getRegisteredApplications(sr.getServiceId());
				if (app != null) {
					serviceNodeNumber = app.getInstances().size();
					List<InstanceInfo> instances = app.getInstances();
					if (instances.size() > 0) {
						RegInfo ri = cms.metadataToRegInfo(instances.get(0).getMetadata(), sr.getServiceId());
						if (ri.getPortal() != null) {
							if(!"".equals(ri.getPortal())){
								portalLink = gateWayServiceUrl + "/" + sr.getPath() + ri.getPortal() + "?sessionId="
										+ req.getRopRequestContext().getSessionId();
							}
						}
					}
				}
				ZuulRoute zr = new ZuulRoute(sr.getId(), "/" + sr.getPath() + "/**", sr.getServiceId(),
						sr.getServiceName(), sr.getUrl(), "1".equals(sr.getStripPrefix()),
						"1".equals(sr.getRetryable()), sensitiveHeaders, "1".equals(sr.getNeedAuth()),
						"1".equals(sr.getEnabled()), serviceNodeNumber, portalLink,sr.getAuthResource());
				result.add(zr);
			}
			response.setRoutes(result);
			LOGGER.info("路由列表查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("路由列表查询异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.route.detail", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RouteDetailResponse getDetail(RouteIdRequest req) {
		LOGGER.debug("===获取路由详情接口(mapps.servicemanager.route.detail)入口,请求参数==" + LogUtil.getObjectInfo(req));
		RouteDetailResponse response = new RouteDetailResponse();
		
		String routeId = req.getRouteId();
		
		SmRoute routeDetail = getDetail(routeId);
		response.setRouteDetail(routeDetail);
		return response;
	}

	public SmRoute getDetail(String routeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("routeId", routeId);
		SmRoute routeDetail = routeMapper.getRouteDetailById(map);
		return routeDetail;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.route.edit", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RouteIdResponse editRoute(RouteDetailRequest req) {
		RouteIdResponse response = new RouteIdResponse();
		try {
			LOGGER.debug("===路由编辑保存接口(mapps.servicemanager.route.edit)入口,请求参数==" + LogUtil.getObjectInfo(req));
			SmRoute route = new SmRoute();
			route.setId(req.getId());
			route.setServiceId(req.getServiceId());
			route.setUrl(req.getUrl());
			route.setServiceName(req.getServiceName());
			route.setPath(req.getPath());
			route.setStripPrefix(req.getStripPrefix());
			route.setRetryable(req.getRetryable());
			route.setSensitiveHeaders(req.getSentitiveHeaders());
			route.setNeedAuth(req.getNeedAuth());
			route.setEnabled(req.getEnabled());
			response.setId(req.getId());
			routeMapper.updateByPrimaryKeySelective(route);
			// 推送通知到网关
			pubsub.publish(ROUTE_UPDATE_CHANNEL, "");
			LOGGER.info("路由编辑保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("路由编辑保存异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.route.addReg", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RouteIdResponse addRegRoute(RouteAddRegRequest req) {
		RouteIdResponse response = new RouteIdResponse();
		try {
			LOGGER.debug("===注册路由新增接口(mapps.servicemanager.route.addReg)入口,请求参数==" + LogUtil.getObjectInfo(req));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("serviceId", req.getServiceId());
			if(!"".equals(req.getPath())){
				map.put("path", "%" + req.getPath() + "%");
			}
			List<SmRoute> RouteListByServiceId = routeMapper.getRouteListByServiceId(map);
			List<SmRoute> RouteListByPath = routeMapper.getRouteListByPath(map);
			if (RouteListByPath.size() > 0) {
				LOGGER.info("路由路径已经存在或冲突");
				ErrorCode.fail(response, ErrorCode.CODE_300010);
			} else if (RouteListByServiceId.size() > 0) {
				LOGGER.info("服务已经添加路由");
				ErrorCode.fail(response, ErrorCode.CODE_300011);
			} else {
				String tempId = IDGen.shortId();
				SmRoute route = new SmRoute();
				route.setId(tempId);
				route.setServiceId(req.getServiceId());
				route.setServiceName(req.getServiceName());
				route.setPath(req.getServiceId());
				//route.setPath(req.getPath());
				route.setStripPrefix(req.getStripPrefix());
				route.setRetryable(req.getRetryable());
				route.setSensitiveHeaders(req.getSsHeaders());
				route.setNeedAuth(req.getNeedAuth());
				route.setEnabled(req.getEnabled());
				route.setAuthResource(req.getAuthResource());
				response.setId(tempId);
				routeMapper.insertSelective(route);

				// 推送通知到网关
				pubsub.publish(ROUTE_UPDATE_CHANNEL, "");
				LOGGER.info("注册路由新增成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("注册路由新增异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.route.testReg", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RouteIdResponse testRegRoute(ServiceIdRequest req) {
		RouteIdResponse response = new RouteIdResponse();
		try {
			LOGGER.debug("===注册路由测试接口(mapps.servicemanager.route.testReg)入口,请求参数==" + LogUtil.getObjectInfo(req));
			// 获取所有接入的服务列表，发送重启指令
			Application app = discoveryClient.getApplication(req.getServiceId());
			if (app != null) {
				List<InstanceInfo> instances = app.getInstances();
				for (InstanceInfo instance : instances) {
					LOGGER.info("================>serviceurl : " + instance.getStatusPageUrl());
					URL url = new URL(instance.getStatusPageUrl());
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					int state = con.getResponseCode();
					LOGGER.info("================>state : " + state);
					if (state == 200) {
					}
				}
			}
			LOGGER.info("注册路由测试成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("注册路由测试异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.route.saveReg", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RouteIdResponse saveRegRoute(RouteSaveRegRequest req) {
		RouteIdResponse response = new RouteIdResponse();
		try {
			LOGGER.debug("===注册路由保存接口(mapps.servicemanager.route.saveReg)入口,请求参数==" + LogUtil.getObjectInfo(req));
			SmRoute route = new SmRoute();
			route.setId(req.getId());
			route.setServiceId(req.getServiceId());
			route.setServiceName(req.getServiceName());
			//route.setPath(req.getPath());
			route.setPath(req.getServiceId());
			route.setStripPrefix(req.getStripPrefix());
			route.setRetryable(req.getRetryable());
			route.setSensitiveHeaders(req.getSsHeaders());
			route.setNeedAuth(req.getNeedAuth());
			route.setEnabled(req.getEnabled());
			route.setAuthResource(req.getAuthResource());
			response.setId(req.getId());
			routeMapper.updateByPrimaryKeySelective(route);

			// 推送通知到网关
			pubsub.publish(ROUTE_UPDATE_CHANNEL, "");
			LOGGER.info("注册路由保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("注册路由保存异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.route.addEx", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RouteIdResponse addExRoute(RouteAddExRequest req) {
		RouteIdResponse response = new RouteIdResponse();
		try {
			LOGGER.debug("===外部路由新增接口(mapps.servicemanager.route.addEx)入口,请求参数==" + LogUtil.getObjectInfo(req));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("path", "%" + req.getPath() + "%");
			List<SmRoute> RouteListByPath = routeMapper.getRouteListByPath(map);
			if (RouteListByPath.size() > 0) {
				LOGGER.info("路由路径已经存在或冲突");
				ErrorCode.fail(response, ErrorCode.CODE_300010);
			} else {
				String tempId = IDGen.shortId();
				SmRoute route = new SmRoute();
				route.setId(tempId);
				route.setUrl(req.getUrl());
				route.setServiceId(req.getServiceName());
				route.setServiceName(req.getServiceName());
				route.setPath(req.getPath());
				route.setStripPrefix(req.getStripPrefix());
				route.setRetryable(req.getRetryable());
				route.setSensitiveHeaders(req.getSsHeaders());
				route.setNeedAuth(req.getNeedAuth());
				route.setEnabled(req.getEnabled());
				route.setAuthResource(req.getAuthResource());
				response.setId(tempId);
				routeMapper.insertSelective(route);

				// 推送通知到网关
				pubsub.publish(ROUTE_UPDATE_CHANNEL, "");
				LOGGER.info("外部路由新增成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("外部路由新增异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.route.saveEx", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RouteIdResponse saveRegRoute(RouteSaveExRequest req) {
		RouteIdResponse response = new RouteIdResponse();
		try {
			LOGGER.debug("===外部路由保存接口(mapps.servicemanager.route.saveReg)入口,请求参数==" + LogUtil.getObjectInfo(req));
			// Map<String, Object> map = new HashMap<String, Object>();
			// map.put("serviceId", req.getServiceId());
			// map.put("path", "%" + req.getPath() + "%");
			SmRoute route = new SmRoute();
			route.setId(req.getId());
			route.setServiceId(req.getServiceName());
			route.setServiceName(req.getServiceName());
			route.setUrl(req.getUrl());
			route.setPath(req.getPath());
			route.setStripPrefix(req.getStripPrefix());
			route.setRetryable(req.getRetryable());
			route.setSensitiveHeaders(req.getSsHeaders());
			route.setNeedAuth(req.getNeedAuth());
			route.setEnabled(req.getEnabled());
			route.setAuthResource(req.getAuthResource());
			response.setId(req.getId());
			routeMapper.updateByPrimaryKeySelective(route);

			// 推送通知到网关
			pubsub.publish(ROUTE_UPDATE_CHANNEL, "");
			LOGGER.info("外部路由保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("外部路由保存异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.route.disable", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RouteIdResponse disableRoute(RouteIdRequest req) {
		RouteIdResponse response = new RouteIdResponse();
		try {
			LOGGER.debug("===路由禁用接口(mapps.servicemanager.route.disable)入口,请求参数==" + LogUtil.getObjectInfo(req));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", req.getRouteId());
			response.setId(req.getRouteId());
			routeMapper.disableRouteById(map);

			// 推送通知到网关
			pubsub.publish(ROUTE_UPDATE_CHANNEL, "");
			LOGGER.info("路由禁用成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("路由禁用异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.route.enable", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RouteIdResponse enableRoute(RouteIdRequest req) {
		RouteIdResponse response = new RouteIdResponse();
		try {
			LOGGER.debug("===路由启用接口(mapps.servicemanager.route.enable)入口,请求参数==" + LogUtil.getObjectInfo(req));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", req.getRouteId());
			response.setId(req.getRouteId());
			routeMapper.enableRouteById(map);
			// 推送通知到网关
			pubsub.publish(ROUTE_UPDATE_CHANNEL, "");
			LOGGER.info("路由启用成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("路由启用异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.route.queryservice", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public ServiceListResponse queryRouteService(AbstractRopRequest req) {
		ServiceListResponse response = new ServiceListResponse();
		try {
			LOGGER.debug("===注册服务查询接口(mapps.servicemanager.route.enable)入口==");
			List<String> services = discovery.getServices();
			List<String> rstservices = new ArrayList<String>();
			if (services.size() > 0) {
				for (String service : services) {
					if (!"mapps-servicemanager".equals(service) && !"mapps-gateway".equals(service)) {
						rstservices.add(service);
					}
				}
			}
			response.setServices(rstservices);
			LOGGER.info("注册服务查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("注册服务查询异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	/**
	 * 如果应用路由不存在，注册系统路由（禁用）
	 * @param instance
	 */
	@Transactional
	public void regRouteIfNotExists(InstanceInfo instance) {
		// 默认路由不启用
		Map<String, Object> map = new HashMap<String, Object>();
		String appId = instance.getAppName().toLowerCase();
		map.put("serviceId", appId);
		map.put("path", "%" + appId + "%");
		List<SmRoute> RouteListByServiceId = routeMapper.getRouteListByServiceId(map);
		List<SmRoute> RouteListByPath = routeMapper.getRouteListByPath(map);
		if (RouteListByServiceId.size() > 0) {
			LOGGER.info(appId+": 服务已经添加路由");
		} else if (RouteListByPath.size() > 0) {
			LOGGER.info(appId+": 路由路径已经存在或冲突");
		} else if(!"mapps-servicemanager".equals(appId)){
			LOGGER.info(appId+": 执行自动注册");
			String tempId = IDGen.shortId();
			SmRoute route = new SmRoute();
			route.setId(tempId);
			route.setServiceId(appId);
			
			RegInfo regInfo = cms.metadataToRegInfo(instance.getMetadata(),appId);
			LOGGER.info(appId+": 获取到appName: "+regInfo.getAppName());
			
			route.setServiceName(regInfo.getAppName());
			route.setPath(appId);
			route.setStripPrefix("1");
			route.setRetryable("1");
			route.setSensitiveHeaders("");
			route.setNeedAuth("0");
			route.setEnabled("1");
			route.setAuthResource("");
			if("mapps-gateway".equals(appId)){
				route.setEnabled("0");
			}
			if("mapps-fileservice".equals(appId)){
				route.setNeedAuth("1");
				route.setAuthResource("/api/**");
			}
			
			try{
				routeMapper.insertSelective(route);
				LOGGER.info(appId+": 自动注册成功");
			} catch(Exception e){
				e.printStackTrace();
				LOGGER.info(appId+": 自动注册失败");
			}
		}
	}

}
