package com.fiberhome.mapps.mssdk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fiberhome.mapps.mssdk.exception.GetConfigFailException;
import com.fiberhome.mapps.mssdk.utils.CodecUtil;
import com.fiberhome.mapps.mssdk.utils.MD5Utils;
import com.fiberhome.mos.core.openapi.rop.client.RestClient;

public class ServiceClient {
	private String configServiceUrl;
	private String appId;

	public ServiceClient(String configServiceUrl, String appId) {
		this.configServiceUrl = configServiceUrl;
		this.appId = appId;
	}

	/**
	 * 从配置服务获取应用的配置，包括应用参数配置、依赖服务接入参数和资源
	 * 
	 * @param retryable
	 * @return
	 * @throws GetConfigFailException
	 */
	public Map<String, String> getConfig(boolean retryable) throws GetConfigFailException {
		// 调用RestClient从应用中获取
		// @See
		// org.springframework.cloud.config.client.ConfigServicePropertySourceLocator
		HashMap<String, String> map = new HashMap<>();
		Map<String, Object> req = new HashMap<>();
		String reqUrl = this.configServiceUrl;
		if ("/".equals(reqUrl.charAt(reqUrl.length() - 1) + "")) {
			reqUrl = reqUrl.substring(0, reqUrl.length() - 1);
		}

		String rcUrl = reqUrl + "/configSvc/" + appId;
		RestClient client = new RestClient(rcUrl, "", Locale.SIMPLIFIED_CHINESE);
		String rs = "";
		JSONObject res = new JSONObject();

		try {
			rs = client.requestForString(req);
			res = JSONObject.parseObject(rs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new GetConfigFailException("获取应用配置失败");
		}

		try {
			JSONArray services = res.getJSONArray("services");
			for (int i = 0; i < services.size(); i++) {
				JSONObject service = services.getJSONObject(i);
				Set<String> serviceKey = service.keySet();
				Iterator<String> it = serviceKey.iterator();
				String svcId = service.getString("svcId");
				while (it.hasNext()) {
					String key = it.next();
					if (!key.equals("svcId")) {
						map.put("services." + svcId + "." + key, service.getString(key));
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new GetConfigFailException("获取应用配置失败");
		}

		try {
			JSONArray config = res.getJSONArray("config");
			for (int i = 0; i < config.size(); i++) {
				JSONObject configObject = config.getJSONObject(i);
				map.put(configObject.getString("key"), configObject.getString("value"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new GetConfigFailException("获取应用配置失败");
		}

		try {
			JSONArray resources = res.getJSONArray("resources");
			for (int i = 0; i < resources.size(); i++) {
				JSONObject resource = resources.getJSONObject(i);
				Set<String> resourceKey = resource.keySet();
				Iterator<String> it = resourceKey.iterator();
				String resId = resource.getString("resId");
				String resCode = resource.getString("resCode");
				while (it.hasNext()) {
					String key = it.next();
					if (key.equals("config")) {
						JSONArray cfgList = resource.getJSONArray("config");
						if (cfgList != null) {
							for (int j = 0; j < cfgList.size(); j++) {
								JSONObject cfg = cfgList.getJSONObject(j);
								String cfgKey = cfg.getString("key");
								String cfgVal = cfg.getString("value");
								if ("password".equals(cfgKey)) {
									String encryptKey = MD5Utils.getMD5String(appId + "FHuma025");
									String decryptVal = new String(CodecUtil.base64Decode(cfgVal));
									cfgVal = (MD5Utils.convertMD5(decryptVal)).replaceAll(encryptKey, "");
									//cfgVal = CodecUtil.aesDecrypt(decryptVal, encryptKey);
								}
								map.put("resources." + resId + "." + resCode + "." + cfgKey, cfgVal);
							}
						}
					}
					// else if (!key.equals("resId")) {
					// map.put("resources." + resId + "." + key,
					// resource.getString(key));
					// }
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new GetConfigFailException("获取应用配置失败");
		}

		// 输出map
		System.out.println(map);

		return map;
	}
}
