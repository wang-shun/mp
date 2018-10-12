package com.fiberhome.mapps.intergration.security.login;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@ConfigurationProperties(prefix = "mplus.login")
public class MplusLogin {
	private final static String postData = "{\"language\":\"0\",\"mpsession\":\"\",\"version\":\"3.0\","
			+ "\"loginname\":\"******\",\"password\":\"*******\","
			+ "\"ecid\":\"fiberhome\",\"esn\":\"861483030123901\",\"imsi\":\"460110143952154\",\"ostype\":\"android\","
			+ "\"ispad\":\"0\",\"osversion\":\"6.0\",\"hsetname\":\"360\",\"hsetmodel\":\"1503-a01\",\"resolution\":\"1080*1920\","
			+ "\"dpi\":\"xhdpi\",\"serverip\":\"miap.cc\",\"wifimac\":\"02:00:00:00:00:00\",\"clientid\":\"com.fiberhome.mobileark\","
			+ "\"clientversion\":\"4.6.0\",\"isroot\":\"0\",\"isvpn\":\"0\",\"isautologin\":\"1\","
			+ "\"logintype\":\"0\",\"identifycode\":\"\",\"network\":\"wifi\"}";

	private static ObjectMapper OM = new ObjectMapper();
	
	static {
		OM.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		// 这个配置的必要性在于，如果服务端添加了新的字段，客户端还要保持可用性
		OM.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	private RestTemplate restTemplate = new RestTemplate();
	
	private String serviceUrl;

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String clientLogin(Map param) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("user-agent", "GAEA-Client");
		headers.set("connection", "keep alive");
		headers.set("accept-encoding", "application/json");
		headers.set("Accept-Charset", "utf-8");  
		headers.set("Secretflag", "0");
		headers.set("Cmd", "LOGIN");
		
		Map requestMap = null;
		try {
			requestMap = OM.readValue(postData, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		requestMap.putAll(param);
		try {
			String body = OM.writeValueAsString(requestMap);
			HttpEntity<String> entity = new HttpEntity<String>(body, headers);
			
			String response = restTemplate.postForObject(serviceUrl, entity, String.class);
			
			System.out.println(response);
			Map obj = OM.readValue(response, Map.class);
			if (obj != null) {
				return (String)obj.get("secrettoken");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
