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
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @ClassName: RopClient
 * @Description: API客户端
 * 
 */

public class RopClient {

	private static final Logger LOG = LoggerFactory.getLogger(RopClient.class);

	private String serverUrl;

	private String appKey;

	private String appSecret;

	private String sessionId;

	private MessageFormat messageFormat;

	private Locale locale;

	private RestTemplate restTemplate;
	
	HttpHeaders headers = new HttpHeaders();

	public RopClient(String serviceUrl, String appKey, String appSecret) {
		this(serviceUrl, appKey, appSecret, "json", Locale.SIMPLIFIED_CHINESE);
	}

	public RopClient(String serviceUrl, String appKey, String appSecret, String format) {
		this(serviceUrl, appKey, appSecret, format, Locale.SIMPLIFIED_CHINESE);
	}

	public RopClient(String serviceUrl, String appKey, String appSecret, String format, Locale locale) {
		this.serverUrl = serviceUrl;
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.messageFormat = MessageFormat.valueOf(format);
		this.locale = locale;

		this.configConfigMapper();

		this.restTemplate = buildRestTemplate();
		
		if (serverUrl.startsWith("https")) {
			final ClientHttpRequestFactory clientHttpRequestFactory = new CustomClientHttpRequestFactory();
			restTemplate.setRequestFactory(clientHttpRequestFactory);
			System.setProperty("https.protocols", "TLSv1");
		}
	}

	private RestTemplate buildRestTemplate() {
		RestTemplate restTemplate = SpringContextUtils.getRestTemplate();
		if (restTemplate == null) {
			restTemplate = new RestTemplate();
		}
		return restTemplate;
	}
	
	public void setHeaders(String headerName, String headerValue) {
		headers.add(headerName, headerValue);
	}

	public String requestForString(String method, Map<String, Object> param) throws RopClientException {
		return this.requestForString(method, null, param);
	}

	public List<Map<String, Object>> requestForList(String method, Map<String, Object> param)
			throws RopClientException {
		return this.requestForList(method, "1.0", param);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> requestForList(String method, String version, Map<String, Object> param)
			throws RopClientException {
		return this.requestForObject(method, version, param, List.class);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> requestForMap(String method, Map<String, Object> param) throws RopClientException {
		return this.requestForObject(method, "1.0", param, Map.class);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> requestForMap(String method, String version, Map<String, Object> param)
			throws RopClientException {
		return this.requestForObject(method, version, param, Map.class);
	}

	public <T> T requestForObject(String method, Map<String, Object> param, Class<T> clz) throws RopClientException {
		return this.requestForObject(method, "1.0", param, clz);
	}

	public <T> T requestForObject(String method, String verion, Map<String, Object> param, Class<T> clz)
			throws RopClientException {
		String str = this.requestForString(method, verion, param);
		try {
			return this.objectMapper.readValue(str, clz);
		} catch (Exception e) {
			LOG.warn("响应解析失败：{}", str, e);
			throw new RopClientException("响应解析失败", e);
		}
	}

	public String requestForString(String method, String version, Map<String, Object> param) throws RopClientException {
		Assert.notNull(method);

		String ret = this.post(this.getSessionId(), this.getServerUrl(), method, version,
				this.createRequestParam(param));

		if (LOG.isDebugEnabled()) {
			LOG.debug("Requsting Method[{}]/Version[{}] with [{}]; Got: {}", method, version, mapToString(param), ret);
		}
		return ret;
	}

	public String post(String sessionId, String serverUrl, String methodName, String version, RequestParams param)
			throws RopClientException {
		try {
			Map<String, Object> requestParams = getRequestForm(sessionId, methodName, version, param);
			
			headers.set("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			// 处理附件上传的header头
			boolean hasFile = false;
			Iterator<String> it = requestParams.keySet().iterator();
			while (it.hasNext()) {
				Object v = requestParams.get(it.next());
				if (v instanceof AbstractResource) {
					headers.set("Content-Type", MediaType.MULTIPART_FORM_DATA.toString());
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
			LOG.error("调用API请求出错！serverUrl:{},methodName:{},version:{}.", serverUrl, methodName, version);
			throw new RopClientException("调用API请求出错！", e);	
		}
	}

	protected Map<String, Object> getRequestForm(String sessionId, String methodName, String version,
			RequestParams param) {
		Map<String, Object> form = new LinkedHashMap<String, Object>(16);

		// 系统级参数
		form.put(SystemParameterNames.getAppKey(), appKey);
		form.put(SystemParameterNames.getMethod(), methodName);
		form.put(SystemParameterNames.getVersion(), version);
		form.put(SystemParameterNames.getFormat(), messageFormat.name());
		form.put(SystemParameterNames.getLocale(), locale.toString());
		if (sessionId != null) {
			form.put(SystemParameterNames.getSessionId(), sessionId);
		}

		// 业务级参数
		form.putAll(ClientUtil.getParamFields(param));

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

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	

}
