package com.fiberhome.mos.core.openapi.rop.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.AbstractResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClient {
	private static final Logger LOG = LoggerFactory.getLogger(RopClient.class);

	private String serverUrl;
	
	private String appSecret;

	private Locale locale;

	private RestTemplate restTemplate;
	
	HttpHeaders headers = new HttpHeaders();
	
	public RestClient(String serviceUrl, String appSecret, Locale locale) {
		this.serverUrl = serviceUrl;
		this.appSecret = appSecret;
		this.locale = locale;
		
		restTemplate = new RestTemplate();
	}
	
	public void setHeaders(String headerName, String headerValue) {
		headers.add(headerName, headerValue);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> requestForList(Map<String, Object> param)
			throws ClientException {
		return this.requestForObject(param, List.class);
	}


	@SuppressWarnings("unchecked")
	public Map<String, Object> requestForMap(Map<String, Object> param)
			throws ClientException {
		return this.requestForObject(param, Map.class);
	}

	public <T> T requestForObject(Map<String, Object> param, Class<T> clz)
			throws ClientException {
		String str = this.requestForString(param);
		try {
			return this.objectMapper.readValue(str, clz);
		} catch (Exception e) {
			LOG.warn("响应解析失败：{}", str, e);
			throw new ClientException("响应解析失败", e);
		}
	}

	public String requestForString(Map<String, Object> param) throws ClientException {
		
		String ret = this.post(this.createRequestParam(param));

		if (LOG.isDebugEnabled()) {
			LOG.debug("Requsting with [{}]; Got: {}", mapToString(param), ret);
		}
		return ret;
	}
	
	public String post(RequestParams param)
			throws ClientException {
		try {
			Map<String, Object> requestParams = getRequestForm(param);
			
			headers.set("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			// 处理附件上传的header头
			boolean hasFile = false;
			Iterator<String> it = requestParams.keySet().iterator();
			while (it.hasNext()) {
				Object v = requestParams.get(it.next());
				if (v instanceof AbstractResource) {
					headers.setContentType(MediaType.MULTIPART_FORM_DATA);
				}
			}

			MultiValueMap<String, Object> multiValueMap = ClientUtil.toMultiValueMap(requestParams);
			HttpEntity entity = new HttpEntity<Object>(multiValueMap, headers);
			
			String responseContent = restTemplate.postForObject(serverUrl, entity, String.class);
			if (LOG.isDebugEnabled()) {
				LOG.debug("response:\n" + responseContent);
			}
			return responseContent;
		} catch (RestClientException e) {
			LOG.error("调用API请求出错！调用参数：{}.", param, e);
			throw new ClientException("调用API请求出错！", e);
		}
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	protected Map<String, Object> getRequestForm(RequestParams param) {
		Map<String, Object> form = new LinkedHashMap<String, Object>(16);
		
		// 业务级参数
		form.putAll(ClientUtil.getParamFields(param));

		if (appSecret != null) {
			List<String> ignoreSignList = ClientUtil.getIgnoreSignFieldNames(param);
			// 对请求进行签名
			Map<String, String> signParam = new LinkedHashMap<String, String>();
			
			Iterator<String> it = form.keySet().iterator();
			while (it.hasNext()) {
				String k = it.next();
				Object v = form.get(k);
				if (v instanceof String) {
					signParam.put(k, (String) v);
				}
			}
			
			String signValue = ClientUtil.sign(ignoreSignList, appSecret, signParam);
			form.put("sign", signValue);
		}
		return form;
	}

	protected ObjectMapper objectMapper = new ObjectMapper();

	protected void configConfigMapper() {
		this.objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		/**
		 * 这个配置的必要性在于，如果服务端添加了新的字段，客户端还要保持可用性
		 */
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	protected String mapToString(Map<String, Object> param) {
		if (null == param) {
			return "null";
		}
		StringBuilder buf = new StringBuilder();
		buf.append("{");

		boolean first = true;
		for (String key : param.keySet()) {
			if (!first) {
				buf.append(",");
			} else {
				first = false;
			}
			buf.append(key).append("=").append(param.get(key));
		}

		buf.append("}");

		return buf.toString();
	}

	protected RequestParams createRequestParam(Map<String, Object> param) {
		List<RequestParam> params = new ArrayList<RequestParam>();
		if (null != param) {
			for (String key : param.keySet()) {
				params.add(new RequestParam(key, param.get(key)));
			}
		}
		return new RequestParams(params);
	}
}
