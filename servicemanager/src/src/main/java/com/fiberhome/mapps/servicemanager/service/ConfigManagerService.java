package com.fiberhome.mapps.servicemanager.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.intergration.utils.CodecUtil;
import com.fiberhome.mapps.intergration.utils.JsonUtil;
import com.fiberhome.mapps.mssdk.ConfigParam;
import com.fiberhome.mapps.mssdk.RegInfo;
import com.fiberhome.mapps.mssdk.Resource;
import com.fiberhome.mapps.redismq.pubsub.PubSub;
import com.fiberhome.mapps.servicemanager.dao.SmAppConfigMapper;
import com.fiberhome.mapps.servicemanager.dao.SmAppMetadataMapper;
import com.fiberhome.mapps.servicemanager.dao.SmDatabaseMapper;
import com.fiberhome.mapps.servicemanager.dao.SmDbAssignMapper;
import com.fiberhome.mapps.servicemanager.dao.SmRedisMapper;
import com.fiberhome.mapps.servicemanager.dao.SmResourceAssignMapper;
import com.fiberhome.mapps.servicemanager.dao.SmResourceConfigMapper;
import com.fiberhome.mapps.servicemanager.dao.SmResourceInfoMapper;
import com.fiberhome.mapps.servicemanager.dao.SmResourceMapper;
import com.fiberhome.mapps.servicemanager.dao.SmRouteMapper;
import com.fiberhome.mapps.servicemanager.dao.SmSvcAuthMapper;
import com.fiberhome.mapps.servicemanager.entity.AppInfo;
import com.fiberhome.mapps.servicemanager.entity.AppRestartStatus;
import com.fiberhome.mapps.servicemanager.entity.ClientInstanceInfo;
import com.fiberhome.mapps.servicemanager.entity.ClientServiceInfo;
import com.fiberhome.mapps.servicemanager.entity.ClientSvcAuthInfo;
import com.fiberhome.mapps.servicemanager.entity.InstanceNode;
import com.fiberhome.mapps.servicemanager.entity.McSystem;
import com.fiberhome.mapps.servicemanager.entity.ResourceAssignInfo;
import com.fiberhome.mapps.servicemanager.entity.RestartResult;
import com.fiberhome.mapps.servicemanager.entity.RsKeyValue;
import com.fiberhome.mapps.servicemanager.entity.RsParamKeyValue;
import com.fiberhome.mapps.servicemanager.entity.SmAppConfig;
import com.fiberhome.mapps.servicemanager.entity.SmAppMetadata;
import com.fiberhome.mapps.servicemanager.entity.SmDatabase;
import com.fiberhome.mapps.servicemanager.entity.SmDbAssign;
import com.fiberhome.mapps.servicemanager.entity.SmRedis;
import com.fiberhome.mapps.servicemanager.entity.SmResource;
import com.fiberhome.mapps.servicemanager.entity.SmResourceAssign;
import com.fiberhome.mapps.servicemanager.entity.SmResourceConfig;
import com.fiberhome.mapps.servicemanager.entity.SmResourceInfo;
import com.fiberhome.mapps.servicemanager.entity.SmRoute;
import com.fiberhome.mapps.servicemanager.entity.SmSvcAuth;
import com.fiberhome.mapps.servicemanager.message.ServiceAuthNotifyMessage;
import com.fiberhome.mapps.servicemanager.request.AppConfigRequest;
import com.fiberhome.mapps.servicemanager.request.AppIdRequest;
import com.fiberhome.mapps.servicemanager.request.AppSaveFormRequest;
import com.fiberhome.mapps.servicemanager.request.ConfigUploadRequest;
import com.fiberhome.mapps.servicemanager.request.QueryListRequest;
import com.fiberhome.mapps.servicemanager.request.ResourceListRequest;
import com.fiberhome.mapps.servicemanager.request.ResourceSetupRequest;
import com.fiberhome.mapps.servicemanager.request.SVCAuthAssignRequest;
import com.fiberhome.mapps.servicemanager.request.SVCAuthGetRequest;
import com.fiberhome.mapps.servicemanager.request.ServerRestartRequest;
import com.fiberhome.mapps.servicemanager.request.ServiceIdRequest;
import com.fiberhome.mapps.servicemanager.response.AppConfigResponse;
import com.fiberhome.mapps.servicemanager.response.AppInfoListResponse;
import com.fiberhome.mapps.servicemanager.response.AppKeyResponse;
import com.fiberhome.mapps.servicemanager.response.AppRestartStatusResponse;
import com.fiberhome.mapps.servicemanager.response.AppSaveFormResponse;
import com.fiberhome.mapps.servicemanager.response.AppStatusResponse;
import com.fiberhome.mapps.servicemanager.response.DatabaseListResponse;
import com.fiberhome.mapps.servicemanager.response.DependenciesResponse;
import com.fiberhome.mapps.servicemanager.response.RedisListResponse;
import com.fiberhome.mapps.servicemanager.response.ResourceGetResponse;
import com.fiberhome.mapps.servicemanager.response.ResourceListResponse;
import com.fiberhome.mapps.servicemanager.response.RouteIdResponse;
import com.fiberhome.mapps.servicemanager.response.SVCAuthGetResponse;
import com.fiberhome.mapps.servicemanager.utils.ErrorCode;
import com.fiberhome.mapps.servicemanager.utils.IDGen;
import com.fiberhome.mapps.servicemanager.utils.LogUtil;
import com.fiberhome.mapps.servicemanager.utils.MD5Utils;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.rop.AbstractRopRequest;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean
public class ConfigManagerService {
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private final static String AUTH_NOTIFY_CHANNEL = "mapps.servicemanager.svcauth.notify";

	@Autowired
	SmResourceAssignMapper resourceAssignMapper;

	@Autowired
	SmSvcAuthMapper svcAuthMapper;

	@Autowired
	SmAppConfigMapper appConfigMapper;

	@Autowired
	SmRouteMapper routeMapper;

	@Autowired
	SmDatabaseMapper databaseMapper;

	@Autowired
	SmRedisMapper redisMapper;

	@Autowired
	SmDbAssignMapper smDbAssignMapper;

	@Autowired
	SmAppMetadataMapper appMetadataMapper;

	@Autowired
	SmResourceInfoMapper resourceInfoMapper;

	@Autowired
	SmResourceMapper resourceMapper;

	@Autowired
	SmResourceConfigMapper resourceConfigMapper;

	// 数据库密码加解密 秘钥
	@Autowired
	DBPasswordEncrypt dbpe;

	@Autowired
	PubSub pubsub;

	@Autowired
	DatabaseManagerService dbms;

	@Autowired
	RedisManagerService rms;

	@Autowired
	private EurekaClient eurekaClient;

	// @Autowired
	// private DiscoveryClient discovery;

	@Autowired
	private McSystemService mcSystemService;

	@Value("${spring.cloud.config.server.native.searchLocations:config}")
	String configLocation;

	@Value("${gateway.serviceUrl}")
	String gateWayServiceUrl;

	@Value("${influxdb.serviceUrl:}")
	String influxDbServiceUrl;

	@Value("${spring.zipkin.baseUrl:http://192.168.160.164:9411}")
	String zipkinUrl;

	// @ServiceMethod(method = "mapps.servicemanager.config.upload", group =
	// "servicemanager", groupTitle = "API", version = "1.0", ignoreSign =
	// IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	@Deprecated
	public BaseResponse uploadConfig(ConfigUploadRequest request) {
		BaseResponse response = new BaseResponse();
		String application = request.getApplication();

		// @TODO 备份原来的配置文件
		try {
			File configFile = getConfigFile(application);

			// Yaml yaml = new Yaml();
			FileUtils.copyInputStreamToFile(request.getFile().getInputStream(), configFile);

		} catch (IOException ex) {
			LOG.error("配置文件存储失败。", ex);
			response.fail("配置文件存储失败。");
		} catch (URISyntaxException ex) {
			LOG.error("配置文件存储路径错误", ex);
			response.fail("配置文件存储失败。");
		}

		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.server.restart", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse serverRestart(ServerRestartRequest request) {
		AppRestartStatusResponse response = new AppRestartStatusResponse();
		String application = request.getApplication();
		AppIdRequest appIdReq = new AppIdRequest();
		appIdReq.setAppId(request.getApplication());
		List<AppRestartStatus> restartStatusList = getServiceRestartStatus(appIdReq).getRestartStatusList();
		try {
			RestartResult rr = restart(application);
			if (!rr.isResult()) {
				if (!"".equals(rr.getIpPort())) {
					for (AppRestartStatus ars : restartStatusList) {
						if (rr.getIpPort().equals(ars.getIp() + ":" + ars.getPort())) {
							ars.setStatus("disconnected");
						}
					}
				}
			}
			response.setRestartStatusList(restartStatusList);
		} catch (IOException e) {
			LOG.error("清除eureka缓存失败", e);
		} catch (Exception e) {
			LOG.error("服务重启失败", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}

		return response;
	}

	// @ServiceMethod(method = "mapps.servicemanager.service.bind", group =
	// "servicemanager", groupTitle = "API", version = "1.0", ignoreSign =
	// IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	// public BaseResponse serviceBind(ServiceBindRequest request) {
	// BaseResponse response = new BaseResponse();
	// // 从svcId中获取应用配置
	// String application = getApplicationFromSvc(request.getSvcId());
	//
	// File configFile = null;
	// FileInputStream fis = null;
	// DumperOptions options = new DumperOptions();
	// options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
	// Yaml yaml = new Yaml(options);
	//
	// // 配置存储，更新到配置文件中
	// Map config = null;
	// try {
	// config = JSONObject.parseObject(request.getConfig(), Map.class);
	// } catch (Exception ex) {
	// LOG.error("配置信息错误，不是正确的json格式。", ex);
	// response.fail("配置信息错误，不是正确的json格式。");
	// return response;
	// }
	//
	// try {
	// configFile = getConfigFile(application);
	// fis = new FileInputStream(configFile);
	// // 读取yml文件
	// Map original = (Map) yaml.load(fis);
	//
	// original.putAll(config);
	// config = original;
	//
	// } catch (Exception e) {
	// LOG.error("原配置文件错误，请检查！", e);
	// response.fail("原配置文件错误，请检查！");
	// return response;
	// } finally {
	// IOUtils.closeQuietly(fis);
	// }
	//
	// // 写入配置
	// try {
	// FileUtils.writeByteArrayToFile(configFile,
	// yaml.dump(config).getBytes("UTF-8"));
	//
	// // 重启服务
	// restart(application);
	// } catch (Exception e) {
	// e.printStackTrace();
	// response.fail("配置写入错误");
	// }
	// return response;
	// }

	@Value("${server.port}")
	String serviceManagerPort;

	private RestartResult restart(String application) throws IOException {
		RestartResult rr = new RestartResult();
		RestTemplate restTemplate = new RestTemplate();
		// 获取所有接入的服务列表，发送重启指令
		Application app = eurekaClient.getApplication(application);

		if (app == null) {
			rr.setResult(false);
			return rr;
		}
		List<InstanceInfo> instances = app.getInstancesAsIsFromEureka();
		for (InstanceInfo instance : instances) {
			String statusUrl = instance.getStatusPageUrl();
			String ipAddr = instance.getIPAddr();
			String mgrPort = instance.getMetadata().get("mgr-port");
			String mgrCtxPath = instance.getMetadata().get("mgr-context-path");

			if ((InstanceStatus.UP.equals(instance.getStatus()) || InstanceStatus.DOWN.equals(instance.getStatus()))
					&& !StringUtils.isEmpty(statusUrl)) {
				String restartUrl = "http://" + ipAddr + ":" + mgrPort + mgrCtxPath + "/restart";
				try {
					app.removeInstance(instance);
					HttpDelete request = new HttpDelete("http://127.0.0.1:" + serviceManagerPort + "/eureka/apps/"
							+ instance.getAppName() + "/" + instance.getInstanceId());
					// HttpResponse httpres =
					HttpClients.createDefault().execute(request);
					// String response =
					restTemplate.postForObject(restartUrl, null, String.class);

				} catch (RestClientException e) {
					LOG.warn("{}/{}:{}不能重新启动", instance.getAppName(), instance.getIPAddr(), instance.getPort(), e);
					rr.setResult(false);
					rr.setIpPort(instance.getIPAddr() + ":" + instance.getPort());
					return rr;
				}
			}
		}
		app.shuffleAndStoreInstances(false);
		rr.setResult(true);
		return rr;
	}

	// private String getApplicationFromSvc(String svcId) {
	// return "mapps-meetingroom";
	// }

	private File getConfigFile(String application) throws URISyntaxException {
		String sept = (configLocation.indexOf("\\") > 0) ? "\\" : "/";
		String uri = configLocation + (configLocation.endsWith(sept) ? "" : sept) + application + "-default.yml";
		return new File(new URI(uri));
	}

	@ServiceMethod(method = "mapps.servicemanager.resource.setup", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse resourceSetup(ResourceSetupRequest req) {
		LOG.debug("===应用资源参数设置接口(mapps.servicemanager.resource.setup)入口,请求参数==" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			String resourceAppId = req.getAppId();
			String resourceAppName = req.getAppId();
			List<AppInfo> applist = getAppList(req).getList();
			if (applist != null) {
				for (AppInfo ai : applist) {
					if (ai.getAppId().equals(req.getAppId())) {
						resourceAppName = ai.getAppName();
					}
				}
			}

			// 清空所有assign(db 等)开始
			SmResourceAssign sraForDelete = new SmResourceAssign();
			sraForDelete.setAppId(req.getAppId());
			List<SmResourceAssign> sraList = resourceAssignMapper.select(sraForDelete);
			for (SmResourceAssign sraOne : sraList) {
				resourceAssignMapper.delete(sraOne);
			}
			SmDbAssign dbaForDelete = new SmDbAssign();
			dbaForDelete.setSvcId(req.getAppId());
			List<SmDbAssign> dbaList = smDbAssignMapper.select(dbaForDelete);
			for (SmDbAssign dbOne : dbaList) {
				smDbAssignMapper.delete(dbOne);
			}
			// 清空所有assign(db 等)结束

			JSONArray resourceList = JSONArray.parseArray(req.getResourceList());
			for (int i = 0; i < resourceList.size(); i++) {
				JSONObject resourceObject = resourceList.getJSONObject(i);
				String resourceDetailId = IDGen.uuid().replaceAll("-", "");
				SmResourceAssign sra = new SmResourceAssign();
				sra.setAppId(resourceAppId);
				sra.setResId(resourceObject.getString("resId"));
				sra.setResCode(resourceObject.getString("resCode"));
				// 检查是否已存在
				// SmResourceAssign srarlt =
				// resourceAssignMapper.selectOne(sra);
				// if (srarlt == null) {

				sra.setAppName(resourceAppName);
				sra.setResName(resourceObject.getString("resName"));
				sra.setId(resourceDetailId);
				sra.setAssignTime(new Date());
				if (!sra.getResId().equals("database") && !sra.getResId().equals("redis")) {
					sra.setReourceId(resourceObject.getString("value"));
				}
				resourceAssignMapper.insertSelective(sra);

				// } else {
				// resourceDetailId = srarlt.getId();
				// }
				if (!"".equals(resourceObject.getString("value"))) {
					// 暂时共用sm_db_assign表,不做判断
					// if (sra.getResId().equals("database")) {
					SmDbAssign dba = new SmDbAssign();
					dba.setAssignId(resourceDetailId);
					dba.setAssignTime(new Date());
					dba.setDbId(resourceObject.getString("value"));
					dba.setSvcId(resourceAppId);
					smDbAssignMapper.insertSelective(dba);
					// }
				}
			}

			LOG.debug("应用资源参数设置成功");
		} catch (Exception e) {
			LOG.error("应用资源参数设置异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.resource.get", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public ResourceGetResponse resourceGetByAppId(AppIdRequest req) {
		LOG.debug("===应用资源参数获取接口(mapps.servicemanager.resource.get)入口,请求参数==" + LogUtil.getObjectInfo(req));
		ResourceGetResponse response = new ResourceGetResponse();
		List<ResourceAssignInfo> raiList = new ArrayList<ResourceAssignInfo>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("appId", req.getAppId());
			raiList = resourceAssignMapper.getResourceListByAppId(map);
			String encryptKey = MD5Utils.getMD5String(req.getAppId() + "FHuma025");
			for (ResourceAssignInfo rai : raiList) {
				if (rai.getResId().equals("database")) {
					Map<String, Object> assignMap = new HashMap<String, Object>();
					assignMap.put("assignId", rai.getId());
					SmDbAssign dbAssign = smDbAssignMapper.getDbAssignById(assignMap);
					if (dbAssign != null) {
						SmDatabase db = new SmDatabase();
						db.setId(dbAssign.getDbId());
						SmDatabase rsdb = databaseMapper.selectOne(db);
						rai.setConfig(getDatabaseResourceObjectInfo(rsdb, encryptKey));
					}
				} else if (rai.getResId().equals("redis")) {
					Map<String, Object> assignMap = new HashMap<String, Object>();
					assignMap.put("assignId", rai.getId());
					SmDbAssign dbAssign = smDbAssignMapper.getDbAssignById(assignMap);
					if (dbAssign != null) {
						SmRedis rds = new SmRedis();
						rds.setId(dbAssign.getDbId());
						SmRedis rsrds = redisMapper.selectOne(rds);
						rai.setConfig(getRedisResourceObjectInfo(rsrds, encryptKey));
					}
				} else {
					List<RsKeyValue> rsJsonMap = new ArrayList<RsKeyValue>();
					SmResourceAssign sra = resourceAssignMapper.selectByPrimaryKey(rai.getId());
					SmResourceConfig src = new SmResourceConfig();
					src.setResourceId(sra.getReourceId());
					src.setActived("1");
					List<SmResourceConfig> resourceConfigList = resourceConfigMapper.select(src);
					for (SmResourceConfig rc : resourceConfigList) {
						RsKeyValue rsJson = new RsKeyValue();
						rsJson.setKey(rc.getParamKey());
						rsJson.setValue(rc.getParamValue());
						rsJsonMap.add(rsJson);
					}
					rai.setConfig(rsJsonMap);
				}
			}
			response.setResources(raiList);
			LOG.debug("应用资源参数获取成功");
		} catch (Exception e) {
			LOG.error("应用资源参数获取异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.svcauth.assign", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse svcauthAssign(SVCAuthAssignRequest req) {
		LOG.debug("===服务鉴权信息分配接口(mapps.servicemanager.svcauth.assign)入口,请求参数==" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			if (req.getServiceList() != null) {
				SmSvcAuth ssaForDel = new SmSvcAuth();
				ssaForDel.setAppId(req.getAppId());
				svcAuthMapper.delete(ssaForDel);
				JSONArray svcList = JSONArray.parseArray(req.getServiceList());
				for (int num = 0; num < svcList.size(); num++) {
					JSONObject svc = svcList.getJSONObject(num);
					SmSvcAuth ssa = new SmSvcAuth();
					ssa.setAppId(req.getAppId());
					List<AppInfo> applist = getAppList(req).getList();
					if (applist != null) {
						for (AppInfo ai : applist) {
							if (ai.getAppId().equals(req.getAppId())) {
								ssa.setAppName(ai.getAppName());
							} else {
								ssa.setAppName(req.getAppId());
							}
						}
					} else {
						ssa.setAppName(req.getAppId());
					}
					ssa.setSvcId(svc.getString("svcId"));
					SmRoute rt = new SmRoute();
					rt.setServiceId(svc.getString("svcId"));
					SmRoute rtslt = routeMapper.selectOne(rt);
					if (rtslt != null) {
						ssa.setSvcName(rtslt.getServiceName());
						ssa.setRouteId(rtslt.getId());
					} else {
						ErrorCode.fail(response, ErrorCode.CODE_300012);
					}
					ssa.setAppkey(svc.getString("appkey"));
					ssa.setSecret(svc.getString("secret"));
					ssa.setAuthTime(new Date());
					if (svc.getString("appkey") != null) {
						svcAuthMapper.insertSelective(ssa);
					}

					// 发布鉴权信息更新
					ServiceAuthNotifyMessage sanMsg = new ServiceAuthNotifyMessage(ssa.getAppId(), ssa.getSvcId());
					pubsub.publish(AUTH_NOTIFY_CHANNEL, JsonUtil.toJson(sanMsg));
				}
			}

			LOG.debug("服务鉴权信息分配成功");
		} catch (Exception e) {
			LOG.error("服务鉴权信息分配异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.svcauth.createappkey", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public AppKeyResponse svcauthCreateAppkey(ServiceIdRequest req) {
		LOG.debug("===服务鉴权信息appkey生成接口(mapps.servicemanager.svcauth.createappkey)入口===");
		AppKeyResponse response = new AppKeyResponse();
		try {
			SmRoute rt = new SmRoute();
			rt.setServiceId(req.getServiceId());
			SmRoute rtslt = routeMapper.selectOne(rt);
			if (rtslt == null) {
				ErrorCode.fail(response, ErrorCode.CODE_300012);
			}
			if ("1".equals(rtslt.getNeedAuth())) {
				response.setAppkey(IDGen.uuid().replaceAll("-", ""));
				response.setSecret(IDGen.shortId());
			}
			LOG.debug("服务鉴权信息分配成功");
		} catch (Exception e) {
			LOG.error("服务鉴权信息分配异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.svcauth.get", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public SVCAuthGetResponse svcauthGet(SVCAuthGetRequest req) {
		LOG.debug("===服务鉴权信息获取接口(mapps.servicemanager.svcauth.get)入口,请求参数==" + LogUtil.getObjectInfo(req));
		SVCAuthGetResponse response = new SVCAuthGetResponse();
		try {
			List<SmSvcAuth> rs = new ArrayList<SmSvcAuth>();
			List<ClientSvcAuthInfo> csList = new ArrayList<ClientSvcAuthInfo>();
			if (req.getAppId() == null && req.getSvcId() == null) {
				rs = svcAuthMapper.selectAll();
			} else {
				SmSvcAuth ssa = new SmSvcAuth();
				ssa.setAppId(req.getAppId());
				ssa.setSvcId(req.getSvcId());
				rs = svcAuthMapper.select(ssa);
			}
			for (SmSvcAuth ssa : rs) {
				ClientSvcAuthInfo csInfo = new ClientSvcAuthInfo();
				csInfo.setAppId(ssa.getAppId());
				csInfo.setSvcId(ssa.getSvcId());
				csInfo.setAppkey(ssa.getAppkey());
				csInfo.setSecret(ssa.getSecret());
				csList.add(csInfo);
			}
			response.setList(csList);
			LOG.debug("服务鉴权信息获取成功");
		} catch (Exception e) {
			LOG.error("服务鉴权信息获取异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.svcauth.delete", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse svcauthDelete(SVCAuthGetRequest req) {
		LOG.debug("===服务鉴权信息删除接口(mapps.servicemanager.svcauth.delete)入口,请求参数==" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			if (req.getAppId() == null || req.getSvcId() == null) {
				ErrorCode.fail(response, ErrorCode.CODE_100001);
			} else {
				SmSvcAuth ssa = new SmSvcAuth();
				ssa.setAppId(req.getAppId());
				ssa.setSvcId(req.getSvcId());
				SmSvcAuth ssaslt = svcAuthMapper.selectOne(ssa);
				svcAuthMapper.delete(ssaslt);
			}
			LOG.debug("服务鉴权信息删除成功");
		} catch (Exception e) {
			LOG.error("服务鉴权信息删除异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.app.status", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public AppStatusResponse getAppStatus(AppIdRequest req) {
		LOG.debug("===应用状态获取接口(mapps.servicemanager.app.status)入口,请求参数==" + LogUtil.getObjectInfo(req));
		AppStatusResponse response = new AppStatusResponse();
		Application app = eurekaClient.getApplication(req.getAppId());
		List<InstanceNode> nodes = new ArrayList<InstanceNode>();
		if (app != null) {
			List<InstanceInfo> instances = app.getInstances();
			for (InstanceInfo instance : instances) {
				InstanceNode node = new InstanceNode();
				node.setInstance(instance.getIPAddr() + ":" + instance.getPort());
				node.setStatus(instance.getStatus().toString());
				nodes.add(node);
			}
			response.setNodes(nodes);
		}
		LOG.debug("应用状态获取成功");
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.app.list", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public AppInfoListResponse getAppList(AbstractRopRequest req) {
		LOG.debug("===应用信息获取接口(mapps.servicemanager.app.list)入口,请求参数==" + LogUtil.getObjectInfo(req));
		AppInfoListResponse response = new AppInfoListResponse();
		try {
			Applications apps = eurekaClient.getApplications();
			List<Application> applist = apps.getRegisteredApplications();
			List<AppInfo> list = new ArrayList<AppInfo>();
			String onLineList = "";
			if (applist.size() > 0) {
				for (Application ap : applist) {
					if (ap != null) {
						List<InstanceInfo> instanceList = ap.getInstancesAsIsFromEureka();

						if (instanceList.size() == 0) {
							continue;
						}
						InstanceInfo oneInstance = instanceList.get(0);
						AppInfo apinfo = new AppInfo();
						try {
							RegInfo ri = metadataToRegInfo(oneInstance.getMetadata(), ap.getName().toLowerCase());
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("serviceId", ap.getName().toLowerCase());
							List<SmRoute> RouteListByServiceId = routeMapper.getRouteListByServiceId(map);
							apinfo.setAppId(ap.getName().toLowerCase());
							apinfo.setInstances(instanceList.size());
							apinfo.setAppName((ri.getAppName() == null) ? ap.getName().toLowerCase() : ri.getAppName());
							String logoPath = "http://" + oneInstance.getHostName() + ":" + oneInstance.getPort();
							apinfo.setLogoPath(logoPath);
							apinfo.setLogo(ri.getLogo());
							apinfo.setPortal(ri.getPortal());
							if (RouteListByServiceId.size() > 0) {
								apinfo.setPath("/" + RouteListByServiceId.get(0).getPath() + "/**");
							}

							if (!"mapps-servicemanager".equalsIgnoreCase(apinfo.getAppId())) {
								list.add(apinfo);
								onLineList += apinfo.getAppId() + ",";
							}
						} catch (Exception e) {
							LOG.warn("服务配置信息读取失败，请检查应用配置信息：{}", e.getMessage(), e);
							apinfo.setInstances(instanceList.size());
							apinfo.setAppName(ap.getName().toLowerCase());
							apinfo.setPath("服务信息读取失败");
						}

					}
				}
			}
			Collections.sort(list, new SortByAppId());
			response.setList(list);

			// 获取离线服务
			List<AppInfo> offLineList = new ArrayList<AppInfo>();
			Map<String, Object> map = new HashMap<String, Object>();
			List<SmRoute> routeList = routeMapper.getAllRoute(map);
			for (SmRoute sr : routeList) {
				if (onLineList.indexOf(sr.getServiceId() + ",") < 0) {
					AppInfo offLineApp = new AppInfo();
					offLineApp.setAppId(sr.getServiceId());
					offLineApp.setAppName(sr.getServiceName());
					offLineApp.setLogoPath("");
					offLineApp.setLogo("logo.png");
					offLineApp.setPath(sr.getPath());
					offLineList.add(offLineApp);
				}
			}
			Collections.sort(offLineList, new SortByAppId());
			response.setOffLineList(offLineList);
			LOG.debug("应用信息获取成功");
		} catch (Exception e) {
			LOG.error("配置存储异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	class SortByAppId implements Comparator<AppInfo> {
		public int compare(AppInfo s1, AppInfo s2) {
			return s1.getAppId().compareTo(s2.getAppId());
		}
	}

	class SortByAppIdDesc implements Comparator<AppInfo> {
		public int compare(AppInfo s1, AppInfo s2) {
			return s2.getAppId().compareTo(s1.getAppId());
		}
	}

	@ServiceMethod(method = "mapps.servicemanager.app.config.save", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse appConfigSave(AppConfigRequest req) {
		LOG.debug("===配置存储接口(mapps.servicemanager.app.config.save)入口,请求参数==" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			if (SessionContext.getUserId() != null) {
				String appId = req.getAppId();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("appId", appId);
				Long curversion = appConfigMapper.getConfigVersion(map);
				if (curversion > 0) {
					map.put("configVersion", curversion);
					appConfigMapper.inactiveConfig(map);
				}
				Long insertversion = curversion + 1;
				if (req.getConfig() != null) {
					JSONArray cftList = JSONArray.parseArray(req.getConfig());
					for (int num = 0; num < cftList.size(); num++) {
						JSONObject cfg = cftList.getJSONObject(num);
						SmAppConfig sac = new SmAppConfig();
						sac.setId(IDGen.uuid().replaceAll("-", ""));
						sac.setAppId(appId);
						sac.setParamKey(cfg.getString("key"));
						sac.setParamValue(cfg.getString("value").trim());
						sac.setConfigVer(insertversion);
						sac.setActived("1");
						sac.setSetupTime(new Date());
						sac.setSetupUser(SessionContext.getUserId());
						appConfigMapper.insertSelective(sac);
					}
				}
				LOG.debug("配置存储成功");
			}
		} catch (Exception e) {
			LOG.error("配置存储异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.app.config.load", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public AppConfigResponse appConfigLoad(AppIdRequest req) {
		LOG.debug("===配置读取接口(mapps.servicemanager.app.config.load)入口,请求参数==" + LogUtil.getObjectInfo(req));
		AppConfigResponse response = new AppConfigResponse();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("appId", req.getAppId());
			List<RsParamKeyValue> origincfglist = appConfigMapper.getConfigInfo(map);
			List<RsKeyValue> cfglist = new ArrayList<RsKeyValue>();
			for (RsParamKeyValue rpkv : origincfglist) {
				RsKeyValue rkv = new RsKeyValue();
				rkv.setKey(rpkv.getParamKey());
				rkv.setValue(rpkv.getParamValue());
				cfglist.add(rkv);
			}

			// 添加应用的随机management的context path
			cfglist.add(rkv("management.context-path", "/" + IDGen.uuid()));

			// 下发eureka配置项:默认以ip注册 preferIpAddress: true
			cfglist.add(rkv("eureka.instance.preferIpAddress", "true"));

			// 下发influx metrics的上传地址
			McSystem mcSystem = mcSystemService.getCurrentSystem();
			String url = influxDbServiceUrl + "/write?db=" + mcSystem.getDb();
			cfglist.add(rkv("metrics.influx.url", url));
			cfglist.add(rkv("metrics.influx.user", mcSystem.getDbUser()));
			cfglist.add(rkv("metrics.influx.password", mcSystem.getDbPasswd()));
			
			// 下发zipkin配置项: 
			cfglist.add(rkv("spring.zipkin.enabled", "true"));
			cfglist.add(rkv("spring.zipkin.baseUrl", zipkinUrl));

			response.setConfig(cfglist);

			LOG.debug("配置读取成功");
		} catch (Exception e) {
			LOG.error("配置读取异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	private RsKeyValue rkv(String key, String value) {
		RsKeyValue rkv = new RsKeyValue();
		rkv.setKey(key);
		rkv.setValue(value);

		return rkv;
	}

	public List<ClientServiceInfo> servicesGet(AppIdRequest req) {
		LOG.debug("===服务鉴权信息获取接口(mapps.servicemanager.svcauth.get)入口,请求参数==" + LogUtil.getObjectInfo(req));
		List<ClientServiceInfo> serviceInfoList = new ArrayList<ClientServiceInfo>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("appId", req.getAppId());
			serviceInfoList = svcAuthMapper.getServiceInfoByAppId(map);
			LOG.debug("服务鉴权信息获取成功");
		} catch (Exception e) {
			LOG.error("服务鉴权信息获取异常：{}", e);
		}
		return parseServiceUrl(serviceInfoList);
	}

	private List<ClientServiceInfo> parseServiceUrl(List<ClientServiceInfo> serviceInfoList) {
		String gateWayServiceUrlex = gateWayServiceUrl;
		if (!"/".equals(gateWayServiceUrl.charAt(gateWayServiceUrl.length() - 1))) {
			gateWayServiceUrlex = gateWayServiceUrl + "/";
		}
		for (ClientServiceInfo csi : serviceInfoList) {
			csi.setServiceUrl(gateWayServiceUrlex + csi.getPath());
		}
		return serviceInfoList;
	}

	@ServiceMethod(method = "mapps.servicemanager.dependecies.get", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public DependenciesResponse getDependencies(AppIdRequest req) {
		LOG.debug("===应用服务及资源依赖获取接口(mapps.servicemanager.dependecies.get)入口,请求参数==" + LogUtil.getObjectInfo(req));
		DependenciesResponse response = new DependenciesResponse();
		try {
			List<ClientInstanceInfo> instanceInfoList = new ArrayList<ClientInstanceInfo>();
			Application app = eurekaClient.getApplication(req.getAppId());
			List<InstanceInfo> instances = app.getInstancesAsIsFromEureka();

			if (app != null && instances.size() > 0) {
				Map<String, String> metaMap = instances.get(0).getMetadata();

				InstanceInfo instance = instances.get(0);
				metaMap.put("homePageUrl", instance.getHomePageUrl());
				metaMap.put("statusPageUrl", instance.getStatusPageUrl());
				metaMap.put("healthCheckUrl", instance.getHealthCheckUrl());

				for (InstanceInfo ii : instances) {
					ClientInstanceInfo cii = new ClientInstanceInfo();
					cii.setId(ii.getInstanceId());
					cii.setHost(ii.getIPAddr());
					cii.setPort(ii.getPort());
					cii.setStatus(ii.getStatus());
					instanceInfoList.add(cii);
				}

				RegInfo ri = metadataToRegInfo(metaMap, req.getAppId());
				List<ClientServiceInfo> services = new ArrayList<ClientServiceInfo>();
				if (ri.getServices() != null) {
					for (String service : ri.getServices()) {
						ClientServiceInfo svc = new ClientServiceInfo();
						Map<String, Object> serviceMap = new HashMap<String, Object>();
						serviceMap.put("serviceId", service);
						List<SmRoute> routeInfo = routeMapper.getRouteListByServiceId(serviceMap);
						String serviceName = "未注册服务";
						String needAuth = "0";
						if (routeInfo.size() > 0) {
							serviceName = routeInfo.get(0).getServiceName();
							needAuth = routeInfo.get(0).getNeedAuth();
						}
						SmSvcAuth sa = new SmSvcAuth();
						sa.setAppId(req.getAppId());
						sa.setSvcId(service);
						SmSvcAuth saslt = svcAuthMapper.selectOne(sa);
						String appKey = null;
						String secret = null;
						if (saslt != null) {
							appKey = (saslt.getAppkey() == null ? "" : saslt.getAppkey());
							secret = (saslt.getSecret() == null ? "" : saslt.getSecret());
						}
						svc.setAppkey(appKey);
						svc.setSecret(secret);
						svc.setSvcId(service);
						svc.setSvcName(serviceName);
						svc.setNeedAuth(needAuth);
						services.add(svc);
					}
				}
				response.setServices(services);
				response.setResources(ri.getResources());
				response.setAppName(ri.getAppName());
				response.setLogo(ri.getLogo());
				response.setRemarks(ri.getRemarks());
				response.setPortal(ri.getPortal());
				response.setMgrContextPath(ri.getMgrContextPath());
				response.setMgrPort(ri.getMgrPort());
				response.setConfigProperties(ri.getConfigProperties());
				response.setInstanceList(instanceInfoList);

				LOG.debug("应用服务及资源依赖获取成功");
			} else {
				throw new Exception("Eureka应用信息获取异常！");
			}
		} catch (Exception e) {
			LOG.error("应用服务及资源依赖获取异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	public RegInfo metadataToRegInfo(Map<String, String> metaMap, String appId) {
		RegInfo regInfo = new RegInfo();
		String configPropsSorts = "";
		List<Resource> resources = new ArrayList<Resource>();
		List<ConfigParam> configProperties = new ArrayList<ConfigParam>();
		Iterator<Map.Entry<String, String>> entries = metaMap.entrySet().iterator();
		AppIdRequest appIdReq = new AppIdRequest();
		appIdReq.setAppId(appId);
		List<RsKeyValue> config = appConfigLoad(appIdReq).getConfig();
		while (entries.hasNext()) {
			Map.Entry<String, String> entry = entries.next();
			String metaKey = entry.getKey();
			String metaValue = entry.getValue() == null ? null : entry.getValue().trim();
			String[] metaKeys = metaKey.split("\\.");

			if (metaKeys[0].equals("configProperties") && metaKeys.length > 1) {
				ConfigParam cp = new ConfigParam();

				String cfgPropSort = "";
				String cfgProperty = "";

				cfgPropSort = metaKeys[1];
				cfgProperty = metaKeys[2];
				if (configPropsSorts.indexOf("No" + cfgPropSort + "No") < 0) {
					cp.setSort(cfgPropSort);
					configPropsSorts += "No" + cfgPropSort + "No,";
				} else {
					int cptempIndex = -1;
					for (ConfigParam cptemp : configProperties) {
						if (cptemp.getSort() != null) {
							if (cptemp.getSort().equals(cfgPropSort)) {
								cp = cptemp;
								cptempIndex = configProperties.indexOf(cptemp);
							}
						}
					}
					if (cptempIndex > -1) {
						configProperties.remove(cptempIndex);
					}
				}

				if (cfgProperty.equals("key")) {
					cp.setKey(metaValue);
				} else if (cfgProperty.equals("name")) {
					cp.setName(metaValue);
				} else if (cfgProperty.equals("type")) {
					cp.setType(metaValue);
				} else if (cfgProperty.equals("size")) {
					cp.setSize(Integer.parseInt(metaValue));
				} else if (cfgProperty.equals("remark")) {
					cp.setRemark(metaValue);
				} else if (cfgProperty.equals("regex")) {
					cp.setRegex(metaValue);
				} else if (cfgProperty.equals("options")) {
					cp.setOptions(metaValue);
				} else if (cfgProperty.equals("group")) {
					cp.setGroup(metaValue);
				} else if (cfgProperty.equals("default")) {
					cp.setDef(metaValue);
				}
				cp.setIsDef("1");
				if (cp.getKey() != null) {
					for (RsKeyValue rj : config) {
						if (cp.getKey().equals(rj.getKey())) {
							cp.setDef(rj.getValue());
							cp.setIsDef("0");
						}
					}
				}
				configProperties.add(cp);
			} else if (metaKeys[0].equals("dependencies")) {
				if (metaKeys[1].equals("resources") && !StringUtils.isEmpty(metaValue)) {
					String[] resourceValue = metaValue.split(",");
					for (String rv : resourceValue) {
						String resId = metaKeys[2];
						Resource rs = new Resource();
						if ("database".equals(resId) || "redis".equals(resId)) {
							rs.setResId(resId);
							rs.setResCode(rv);
							SmResourceAssign ra = new SmResourceAssign();
							ra.setAppId(appId);
							ra.setResId(rs.getResId());
							ra.setResCode(rs.getResCode());
							SmResourceAssign raslt = resourceAssignMapper.selectOne(ra);
							if (raslt != null) {
								// 暂时共用sm_db_assign表,不做判断
								// if (raslt.getResId().equals("database")) {
								SmDbAssign dba = new SmDbAssign();
								dba.setAssignId(raslt.getId());
								SmDbAssign dbaslt = smDbAssignMapper.selectOne(dba);
								if (dbaslt != null) {
									rs.setAssignedResourceId(dbaslt.getDbId());
								}
								// }
							}
							resources.add(rs);
						} else {
							SmResourceInfo ri = resourceInfoMapper.selectByPrimaryKey(resId);
							if (ri != null) {
								rs.setResId(ri.getId());
								rs.setResCode(rv);
								SmResourceAssign ra = new SmResourceAssign();
								ra.setAppId(appId);
								ra.setResId(rs.getResId());
								ra.setResCode(rs.getResCode());
								SmResourceAssign raslt = resourceAssignMapper.selectOne(ra);
								if (raslt != null) {
									SmDbAssign dba = new SmDbAssign();
									dba.setAssignId(raslt.getId());
									SmDbAssign dbaslt = smDbAssignMapper.selectOne(dba);
									if (dbaslt != null) {
										rs.setAssignedResourceId(dbaslt.getDbId());
									}
								}
								rs.setResName(ri.getName());
								resources.add(rs);
							}
						}
					}
				} else if (metaKeys[1].equals("services") && !StringUtils.isEmpty(metaValue)) {
					regInfo.setServices(metaValue.split(","));
				}
			} else if (metaKey.equals("homePageUrl")) {
				regInfo.setHomePageUrl(metaValue);
			} else if (metaKey.equals("statusPageUrl")) {
				regInfo.setStatusPageUrl(metaValue);
			} else if (metaKey.equals("healthCheckUrl")) {
				regInfo.setHealthCheckUrl(metaValue);
			} else if (metaKey.equals("appName")) {
				regInfo.setAppName(metaValue);
			} else if (metaKey.equals("logo")) {
				regInfo.setLogo(metaValue);
			} else if (metaKey.equals("remarks")) {
				regInfo.setRemarks(metaValue);
			} else if (metaKey.equals("portal")) {
				regInfo.setPortal(metaValue);
			} else if (metaKey.equals("mgr-port")) {
				regInfo.setMgrPort(metaValue);
			} else if (metaKey.equals("mgr-context-path")) {
				regInfo.setMgrContextPath(metaValue);
			}
		}

		for (int i = 0; i < configProperties.size(); i++) {
			ConfigParam cptemp;
			int minIndex = i;
			int minSort = Integer.parseInt(configProperties.get(i).getSort());
			for (int j = (i + 1); j < configProperties.size(); j++) {
				if (minSort > Integer.parseInt(configProperties.get(j).getSort())) {
					minIndex = j;
					minSort = Integer.parseInt(configProperties.get(j).getSort());
				}
			}
			if (minIndex != i) {
				cptemp = configProperties.get(minIndex);
				configProperties.set(minIndex, configProperties.get(i));
				configProperties.set(i, cptemp);
			}
		}

		regInfo.setConfigProperties(configProperties);
		regInfo.setResources(parseResources(resources));
		return regInfo;
	}

	private List<Resource> parseResources(List<Resource> resources) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("database", "数据库");
		map.put("filesystem", "文件服务");
		map.put("redis", "redis");
		for (Resource rs : resources) {
			String resourceKey = rs.getResId();
			Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry<String, String> entry = entries.next();
				String mapKey = entry.getKey();
				String mapValue = entry.getValue();
				if (resourceKey.equals(mapKey)) {
					rs.setResName(mapValue);
				}
			}
		}
		return resources;
	}

	@ServiceMethod(method = "mapps.servicemanager.app.saveform", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public AppSaveFormResponse appSaveform(AppSaveFormRequest req) {
		LOG.debug("===应用表单参数保存接口(mapps.servicemanager.app.saveform)入口,请求参数==" + LogUtil.getObjectInfo(req));
		AppSaveFormResponse response = new AppSaveFormResponse();
		try {
			// if (req.isConfigChange()) {
			AppConfigRequest acReq = new AppConfigRequest();
			acReq.setAppId(req.getAppId());
			acReq.setConfig(req.getConfig());
			appConfigSave(acReq);
			// }

			if (req.isResourceChange()) {
				ResourceSetupRequest rsReq = new ResourceSetupRequest();
				rsReq.setAppId(req.getAppId());
				rsReq.setResourceList(req.getResourceList());
				resourceSetup(rsReq);
			}

			if (req.isServiceChange()) {
				SVCAuthAssignRequest aarReq = new SVCAuthAssignRequest();
				aarReq.setAppId(req.getAppId());
				aarReq.setServiceList(req.getServiceList());
				svcauthAssign(aarReq);
			}

			// 保存metadata,供离线服务参数获取使用
			SmAppMetadata appMetadata = new SmAppMetadata();
			appMetadata.setAppId(req.getAppId());
			// List<ServiceInstance> siList =
			// discovery.getInstances(req.getAppId());
			Applications apps = eurekaClient.getApplications();
			Application app = apps.getRegisteredApplications(req.getAppId());
			List<InstanceInfo> instances = app.getInstancesAsIsFromEureka();
			if (instances.size() > 0) {
				Map<String, String> metaMap = instances.get(0).getMetadata();
				Iterator<Entry<String, String>> it = metaMap.entrySet().iterator();
				JSONObject oneMetadata = new JSONObject();
				String key;
				String value;
				while (it.hasNext()) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
					key = entry.getKey().toString();
					value = entry.getValue().toString();
					oneMetadata.put(key, value);
				}
				appMetadata.setMetadata(oneMetadata.toJSONString());
				SmAppMetadata amslt = appMetadataMapper.selectByPrimaryKey(req.getAppId());
				if (amslt != null) {
					appMetadataMapper.updateByPrimaryKeySelective(appMetadata);
				} else {
					appMetadataMapper.insertSelective(appMetadata);
				}
				response.setIsLive("1");
			} else {
				response.setIsLive("0");
			}

			LOG.debug("应用表单参数保存成功");
		} catch (Exception e) {
			LOG.error("应用表单参数保存异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	// public static List<RsKeyValue> getResourceObjectInfo(Object obj) {
	// try {
	// List<RsKeyValue> rsJsonMap = new ArrayList<RsKeyValue>();
	// // 得到类对象
	// Class userCla = (Class) obj.getClass();
	// /*
	// * 得到类中的所有属性集合
	// */
	// Field[] fs = userCla.getDeclaredFields();
	// for (int i = 0; i < fs.length; i++) {
	// RsKeyValue rsJson = new RsKeyValue();
	// Field f = fs[i];
	// f.setAccessible(true); // 设置些属性是可以访问的
	// Object val = f.get(obj);// 得到此属性的值
	// rsJson.setKey(f.getName());
	// rsJson.setValue(val.toString());
	// rsJsonMap.add(rsJson);
	// }
	// return rsJsonMap;
	// } catch (Exception e) {
	//
	// }
	// return null;
	// }

	public List<RsKeyValue> getDatabaseResourceObjectInfo(Object obj, String encryptKey) {
		try {
			List<RsKeyValue> rsJsonMap = new ArrayList<RsKeyValue>();
			// 得到类对象
			SmDatabase db = (SmDatabase) obj;
			RsKeyValue rsJson = new RsKeyValue();
			rsJson.setKey("type");
			rsJson.setValue(db.getDbType());
			rsJsonMap.add(rsJson);

			rsJson = new RsKeyValue();
			rsJson.setKey("host");
			rsJson.setValue(db.getHost());
			rsJsonMap.add(rsJson);

			rsJson = new RsKeyValue();
			rsJson.setKey("port");
			rsJson.setValue(db.getPort().toString());
			rsJsonMap.add(rsJson);

			rsJson = new RsKeyValue();
			rsJson.setKey("db");
			String sidOrDbName = "";
			if (db.getDbType().equals("oracle")) {
				sidOrDbName = db.getSid();
			} else if (db.getDbType().equals("postgresql")) {
				sidOrDbName = db.getDbName();
			} else if (db.getDbType().equals("mysql")) {
				sidOrDbName = db.getDbName();
			}
			rsJson.setValue(sidOrDbName);
			rsJsonMap.add(rsJson);

			rsJson = new RsKeyValue();
			rsJson.setKey("username");
			rsJson.setValue(db.getUserName());
			rsJsonMap.add(rsJson);

			rsJson = new RsKeyValue();
			rsJson.setKey("password");
			String realPassword = CodecUtil.aesDecrypt(db.getPassword(), DBPasswordEncrypt.encryptKey);
			//String encryptPassword = CodecUtil.aesEncrypt(realPassword, encryptKey);
			rsJson.setValue(CodecUtil.base64Encode(MD5Utils.convertMD5(realPassword+encryptKey).getBytes()));
			rsJsonMap.add(rsJson);
			return rsJsonMap;
		} catch (Exception e) {

		}
		return null;
	}

	public List<RsKeyValue> getRedisResourceObjectInfo(Object obj, String encryptKey) {
		try {
			List<RsKeyValue> rsJsonMap = new ArrayList<RsKeyValue>();
			// 得到类对象
			SmRedis rds = (SmRedis) obj;
			RsKeyValue rsJson = new RsKeyValue();
			rsJson.setKey("host");
			rsJson.setValue(rds.getHost());
			rsJsonMap.add(rsJson);

			rsJson = new RsKeyValue();
			rsJson.setKey("port");
			rsJson.setValue(rds.getPort().toString());
			rsJsonMap.add(rsJson);

			rsJson = new RsKeyValue();
			rsJson.setKey("dbIndex");
			rsJson.setValue(rds.getDbIndex() + "");
			rsJsonMap.add(rsJson);

			rsJson = new RsKeyValue();
			rsJson.setKey("password");
			String realPassword = CodecUtil.aesDecrypt(rds.getPassword(), DBPasswordEncrypt.encryptKey);
			//String encryptPassword = CodecUtil.aesEncrypt(realPassword, encryptKey);
			rsJson.setValue(CodecUtil.base64Encode(MD5Utils.convertMD5(realPassword+encryptKey).getBytes()));
			rsJsonMap.add(rsJson);
			return rsJsonMap;
		} catch (Exception e) {

		}
		return null;
	}

	@ServiceMethod(method = "mapps.servicemanager.service.restartstatus", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public AppRestartStatusResponse getServiceRestartStatus(AppIdRequest req) {
		LOG.debug("===服务重启状态获取接口(mapps.servicemanager.service.restartstatus)入口===");
		AppRestartStatusResponse response = new AppRestartStatusResponse();
		try {
			// Application app = eurekaClient.getApplication(req.getAppId());
			Applications apps = eurekaClient.getApplications();
			Application app = apps.getRegisteredApplications(req.getAppId());
			if (app != null) {
				List<AppRestartStatus> restartStatusList = new ArrayList<AppRestartStatus>();
				List<InstanceInfo> instances = app.getInstancesAsIsFromEureka();
				for (InstanceInfo instance : instances) {
					AppRestartStatus ars = new AppRestartStatus();
					ars.setAppName(instance.getAppName());
					ars.setIp(instance.getIPAddr());
					ars.setPort(instance.getPort());
					if (InstanceStatus.UP.equals(instance.getStatus())) {
						ars.setStatus("up");
					} else if (InstanceStatus.DOWN.equals(instance.getStatus())) {
						ars.setStatus("down");
					} else if (InstanceStatus.STARTING.equals(instance.getStatus())) {
						ars.setStatus("starting");
					} else if (InstanceStatus.OUT_OF_SERVICE.equals(instance.getStatus())) {
						ars.setStatus("outofservice");
					} else if (InstanceStatus.UNKNOWN.equals(instance.getStatus())) {
						ars.setStatus("unknown");
					}
					restartStatusList.add(ars);
				}
				response.setRestartStatusList(restartStatusList);
			}
			LOG.debug("服务重启状态获取成功");
		} catch (Exception e) {
			LOG.error("服务重启状态获取异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.resourcelist.get", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public ResourceListResponse getResourceList(ResourceListRequest req) {
		LOG.debug("===服务配置获取资源接口(mapps.servicemanager.resourcelist.get)入口,请求参数==" + LogUtil.getObjectInfo(req));
		ResourceListResponse response = new ResourceListResponse();
		Map<String, Object> resMap = new HashMap<String, Object>();
		QueryListRequest qlr = new QueryListRequest();
		qlr.setIsenabled("1");
		qlr.setLimit(100);
		qlr.setOffset(1);
		try {
			JSONArray resources = JSONArray.parseArray(req.getResources());
			for (int i = 0; i < resources.size(); i++) {
				String resId = resources.getJSONObject(i).getString("resId");
				if ("database".equals(resId)) {
					// 获取数据库列表
					DatabaseListResponse dblr = dbms.getList(qlr);
					// response.setDatabases(dblr.getDatabases());
					resMap.put(resId, dblr.getDatabases());
				} else if ("redis".equals(resId)) {
					// 获取redis列表
					RedisListResponse rlr = rms.getRedisList(qlr);
					// response.setRedisList(rlr.getRedisList());
					resMap.put(resId, rlr.getRedisList());
				} else {
					SmResource rs = new SmResource();
					rs.setResId(resId);
					rs.setEnabled("1");
					List<SmResource> resouceList = resourceMapper.select(rs);
					resMap.put(resId, resouceList);
				}
			}
			response.setResMap(resMap);
			LOG.debug("服务配置获取资源成功");
		} catch (Exception e) {
			LOG.error("服务配置获取资源异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.dependecies.offget", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public DependenciesResponse offGetDependencies(AppIdRequest req) {
		LOG.debug("===离线应用服务及资源依赖获取接口(mapps.servicemanager.dependecies.offget)入口,请求参数==" + LogUtil.getObjectInfo(req));
		DependenciesResponse response = new DependenciesResponse();
		try {
			SmAppMetadata am = appMetadataMapper.selectByPrimaryKey(req.getAppId());
			if (am != null) {
				JSONObject metadata = JSONObject.parseObject(am.getMetadata());
				Map<String, String> metaMap = new HashMap<String, String>();
				Iterator<String> it = metadata.keySet().iterator();
				String key;
				String value;
				while (it.hasNext()) {
					key = (String) it.next();
					value = metadata.getString(key);
					metaMap.put(key, value);
				}

				RegInfo ri = metadataToRegInfo(metaMap, req.getAppId());
				List<ClientServiceInfo> services = new ArrayList<ClientServiceInfo>();
				if (ri.getServices() != null) {
					for (String service : ri.getServices()) {
						ClientServiceInfo svc = new ClientServiceInfo();
						Map<String, Object> serviceMap = new HashMap<String, Object>();
						serviceMap.put("serviceId", service);
						List<SmRoute> routeInfo = routeMapper.getRouteListByServiceId(serviceMap);
						String serviceName = "未注册服务";
						String needAuth = "0";
						if (routeInfo.size() > 0) {
							serviceName = routeInfo.get(0).getServiceName();
							needAuth = routeInfo.get(0).getNeedAuth();
						}
						SmSvcAuth sa = new SmSvcAuth();
						sa.setAppId(req.getAppId());
						sa.setSvcId(service);
						SmSvcAuth saslt = svcAuthMapper.selectOne(sa);
						String appKey = null;
						String secret = null;
						if (saslt != null) {
							appKey = (saslt.getAppkey() == null ? "" : saslt.getAppkey());
							secret = (saslt.getSecret() == null ? "" : saslt.getSecret());
						}
						svc.setAppkey(appKey);
						svc.setSecret(secret);
						svc.setSvcId(service);
						svc.setSvcName(serviceName);
						svc.setNeedAuth(needAuth);
						services.add(svc);
					}
				}
				response.setServices(services);
				response.setResources(ri.getResources());
				response.setAppName(ri.getAppName());
				response.setLogo(ri.getLogo());
				response.setRemarks(ri.getRemarks());
				response.setPortal(ri.getPortal());
				response.setMgrContextPath(ri.getMgrContextPath());
				response.setMgrPort(ri.getMgrPort());
				response.setConfigProperties(ri.getConfigProperties());
			}
			List<ClientInstanceInfo> instanceList = new ArrayList<ClientInstanceInfo>();
			response.setInstanceList(instanceList);
			LOG.debug("离线应用服务及资源依赖获取成功");
		} catch (Exception e) {
			LOG.error("离线应用服务及资源依赖获取异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.dependecies.offdelete", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RouteIdResponse deleteRoute(AppIdRequest req) {
		RouteIdResponse response = new RouteIdResponse();
		try {
			LOG.debug("===路由删除接口(mapps.servicemanager.dependecies.offdelete)入口,请求参数==" + LogUtil.getObjectInfo(req));
			String appId = req.getAppId();
			// 删除操作
			SmAppConfig sac = new SmAppConfig();
			sac.setAppId(appId);
			appConfigMapper.delete(sac);

			SmAppMetadata sam = new SmAppMetadata();
			sam.setAppId(appId);
			appMetadataMapper.delete(sam);

			SmDbAssign sdm = new SmDbAssign();
			sdm.setSvcId(appId);
			smDbAssignMapper.delete(sdm);

			SmResourceAssign sra = new SmResourceAssign();
			sra.setAppId(appId);
			resourceAssignMapper.delete(sra);

			SmSvcAuth ssa = new SmSvcAuth();
			ssa.setAppId(appId);
			svcAuthMapper.delete(ssa);

			SmRoute sr = new SmRoute();
			sr.setServiceId(appId);
			routeMapper.delete(sr);
			// 推送通知到网关
			final String ROUTE_UPDATE_CHANNEL = "mapps.servicemanager.route.channel";
			pubsub.publish(ROUTE_UPDATE_CHANNEL, "");
			LOG.info("路由删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("路由删除异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.aco.test", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse acoTest(AbstractRopRequest req) {
		LOG.debug("===Aco测试接口(mapps.servicemanager.aco.test)入口,请求参数==" + LogUtil.getObjectInfo(req));
		AppRestartStatusResponse response = new AppRestartStatusResponse();
		try {
			// ServiceClient sc = new
			// ServiceClient("http://127.0.0.1:8761/","mapps-fileservice");
			// sc.getConfig(true);
			Application app = eurekaClient.getApplication("mapps-testapp");
			if (app != null) {
				List<InstanceInfo> instances = app.getInstances();
				for (InstanceInfo instance : instances) {
					app.removeInstance(instance);
					HttpDelete request = new HttpDelete("http://127.0.0.1:" + serviceManagerPort + "/eureka/apps/"
							+ instance.getAppName() + "/" + instance.getInstanceId());
					HttpClients.createDefault().execute(request);
				}
				app.shuffleAndStoreInstances(false);
			}
			LOG.debug("Aco测试成功");
		} catch (Exception e) {
			LOG.error("Aco测试异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
}
