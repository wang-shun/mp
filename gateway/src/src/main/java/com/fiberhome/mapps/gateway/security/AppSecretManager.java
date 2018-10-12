package com.fiberhome.mapps.gateway.security;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fiberhome.mapps.gateway.security.token.TokenResponse;
import com.fiberhome.mapps.gateway.utils.EurekaClientUtils;
import com.fiberhome.mapps.redismq.pubsub.PubSub;
import com.fiberhome.mapps.redismq.utils.JsonUtil;
import com.fiberhome.mapps.utils.IDGen;
import com.fiberhome.mos.core.openapi.rop.client.RopClient;
import com.fiberhome.mos.core.openapi.rop.client.RopClientException;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.rop.utils.RopUtils;

public class AppSecretManager {
	private final static String AUTH_NOTIFY_CHANNEL = "mapps.servicemanager.svcauth.notify"; 
	private static Logger LOG = LoggerFactory.getLogger(AppSecretManager.class);
	
	private static AtomicBoolean LISTENER_INITED = new AtomicBoolean(false);
	
	EurekaClientUtils clientUtils;
	StringRedisTemplate template;
	PubSub pubsub;
	
	ConcurrentHashMap<String, List<AppSecretInfo>> appIdSecretMap = new ConcurrentHashMap<>();
	ConcurrentHashMap<String, AppSecretInfo> appkeySecretMap = new ConcurrentHashMap<>();
	
	ArrayList<String> ignoreList = new ArrayList<String>();	
	
	public AppSecretManager(EurekaClientUtils clientUtils, StringRedisTemplate template, PubSub pubsub) {
		this.clientUtils = clientUtils;
		this.template = template;
		this.pubsub = pubsub;
		
		ignoreList.add("sign");
		updateAll();
		// 注册appSecret变动listener
		registNotifyListener();
	}

	public AppSecretInfo getSecretInfo(String appkey) {
		return appkeySecretMap.get(appkey);
	}
	
	/**
	 * 生成token
	 * @param appkey
	 * @param secret
	 * @param timeout 单位秒
	 * @return
	 */
	public TokenResponse generateAccessToken(String appkey, String secret, int timeout) {
		// 从鉴权信息中获取
		if (!isValidate(appkey, secret)) {
			return TokenResponse.fail();
		}
		String token = IDGen.uuid();
		
		template.opsForValue().append(token, "appid+svcId");
		template.expire(token, timeout, TimeUnit.SECONDS);
		
		return TokenResponse.success(token, timeout);
	}
	
	public boolean isValidate(String appkey, String secret) {
		AppSecretInfo appSecret = this.getSecretInfo(appkey);
		if (secret != null && appSecret != null && secret.equals(appSecret.getSecret())) {
			return true;
		}
		return false;
	}
	
	public boolean isValidate(String token) {
		return template.opsForValue().get(token) != null;
	}
	
	public void updateAppSecretInfo(String appId) {
		// 清除
		List<AppSecretInfo> list = appIdSecretMap.remove(appId);
		for (AppSecretInfo asi : list) {
			appkeySecretMap.remove(asi.getAppkey());
		}
		
		// 获取新的应用配置信息
		List<AppSecretInfo> result = fetchSecretInfo(appId, null);
		if (result != null) {
			for (AppSecretInfo asi : result) {
				
				String appkey = asi.getAppkey();
				if (appkey == null) {
					continue;
				}
				appkeySecretMap.put(appkey, asi);
			}
		}
		
		appIdSecretMap.put(appId, result);
	}
	
	public void updateAll() {
		appIdSecretMap.clear();
		appkeySecretMap.clear();
		
		// 获取新的应用配置信息
		List<AppSecretInfo> result = fetchSecretInfo(null, null);
		if (result != null) {
			for (AppSecretInfo asi : result) {
				String appId = asi.getAppId();
				List<AppSecretInfo> svcList = appIdSecretMap.get(appId);
				if (svcList == null) {
					svcList = new ArrayList<>();
					appIdSecretMap.put(appId, svcList);
				}
				svcList.add(asi);
				String appkey = asi.getAppkey();
				if (appkey == null) {
					continue;
				}
				appkeySecretMap.put(appkey, asi);
			}
		}
	}
	
	private List<AppSecretInfo> fetchSecretInfo(String appId, String svcId) {
		RopClient client = clientUtils.getServiceManagerClient();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("v", "1.0");
		param.put("appId", appId);
		param.put("svcId", svcId);
		
		try {
			AppSecretResponse response = client.requestForObject("mapps.servicemanager.svcauth.get", param, AppSecretResponse.class);
			
			if (response != null) {
				return response.getList();
			}
		} catch (RopClientException e) {
			LOG.error("获取服务鉴权失败, 请检查ServiceManager接口mapps.servicemanager.svcauth.get服务是否正常。", e);
		}
		
		return null;
	}
	
	private void registNotifyListener() {
		if (LISTENER_INITED.get()) return;
		
		pubsub.subscribe(AUTH_NOTIFY_CHANNEL, new MessageListener() {
			@Override
			public void onMessage(Message message, byte[] pattern) {
				
				try {
					String msg = new String(message.getBody(), "UTF-8");
					LOG.info("服务鉴权信息更新：{}" + msg);
					ServiceAuthNotifyMessage sanMsg = (ServiceAuthNotifyMessage)JsonUtil.jsonToObject(msg, ServiceAuthNotifyMessage.class);
					AppSecretManager.this.updateAppSecretInfo(sanMsg.getAppId());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
			}
		});
	}

	public boolean isValidate(HttpServletRequest request) {
		String appkey = request.getParameter("appKey");
		if (appkey == null) {
			appkey = request.getParameter("appkey");
		}
		
		if (appkey == null) {
			return false;
		}
		AppSecretInfo appSecret = this.getSecretInfo(appkey);
		String sign = request.getParameter("sign");
		
		if (appSecret == null || sign == null) {
			LOG.debug("鉴权失败，鉴权信息不完整，appSecret:{}, sign:{}",  appSecret, sign);
			return false;
		}
		
		Enumeration<String> names = request.getParameterNames();
		HashMap<String, String> params = new HashMap<>();
		while(names.hasMoreElements()) {
			String key = names.nextElement();
			params.put(key, request.getParameter(key));
		}
		
		String calcSign = RopUtils.sign(params, ignoreList, appSecret.getSecret());
		
		return sign.equals(calcSign);
	}
	
	
	
}

