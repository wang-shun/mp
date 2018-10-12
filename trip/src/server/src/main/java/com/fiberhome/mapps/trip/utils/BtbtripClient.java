package com.fiberhome.mapps.trip.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BtbtripClient {

	//测试环境
//	private static final String APPID = "10004" ;
//	private static final String APPKEY = "f600f42f03084084bb7e0d4912259ca0" ;
//	private static final String CHANNELCODE = "nanjingfenghuo" ;
//	private static final String SECRET = "e8cfde69647345d58391c6594531ec2c" ;
//	private static final String BTBTRIP_URL = "http://112.74.210.200:8181/open-api/" ;
	
	//生产环境
	private static final String APPID = "10102" ;
	private static final String APPKEY = "70b4229f2556447fad7e7a19f4710719" ;
	private static final String CHANNELCODE = "nanjingfenghuo" ;
	private static final String SECRET = "a280954cf6fc43728f16ae16b208457f" ;
	private static final String BTBTRIP_URL = "http://api.btbtrip.com/" ;
	
	
	private static RestTemplate restTemplate = new RestTemplate();
	
	protected final static Logger     LOGGER        = LoggerFactory.getLogger(BtbtripClient.class);
	/**
	 * 请求铂旅接口
	 * @param url  铂旅接口URL
	 * @param requestContent
	 * @return
	 */
	public static JSONObject sendRequest(String apiUrl,Map<String,Object> param) {
		
		String timestamp = new Date().getTime() + "";
		
		List<HttpMessageConverter<?>> converterList=restTemplate.getMessageConverters();
	    HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
	    converterList.add(0, converter);
	    restTemplate.setMessageConverters(converterList);
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-ATTA-APPID", APPID);
		headers.set("X-ATTA-APPKEY", APPKEY);
		headers.set("X-ATTA-TIMESTAMP", timestamp);
		headers.set("X-ATTA-CHANNELCODE", CHANNELCODE);
		String token = TripTokenUtil.getToken(APPID,APPKEY, CHANNELCODE,SECRET, timestamp);
		headers.set("X-AUTH-HEADER", token);
		headers.set("Accept", "application/json");
		headers.set("Content-Type", "application/json");
		String body = getRequsetBody(param);
		System.out.println(body);
		HttpEntity entity = new HttpEntity(body, headers);
		String response = "" ;
		try {
		    
			response = (String)restTemplate.postForObject(BTBTRIP_URL + apiUrl, entity,String.class);
			LOGGER.info("send request to btbtrip , request url : "+apiUrl+" request body : "+body+" response : "+response);
		}catch (Exception e) {
//			String msg = e.getMessage() ;
			response = "{\"status\":9,\"msg\":\"数据接口访问失败\"}" ;
			e.printStackTrace();
			LOGGER.info("send request to btbtrip , request url : "+apiUrl+" request body : "+body+" response : "+response);
			LOGGER.error("send request to btbtrip , request url : "+apiUrl+" error : "+e.getMessage());
		}
//		System.out.println(response);
		return JSONObject.parseObject(response);
	}
	
	private static String getRequsetBody(Map<String,Object> param){
		
		if(param == null){
			return null ;
		}
		ObjectMapper om = new ObjectMapper();
		try {
			return om.writeValueAsString(param);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		} 
	}
	
}
