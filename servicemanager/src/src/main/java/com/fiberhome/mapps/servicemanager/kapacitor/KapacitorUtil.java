package com.fiberhome.mapps.servicemanager.kapacitor;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class KapacitorUtil {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private String kapacitorServiceUrl;

	public KapacitorUtil(String kapacitorServiceUrl) {
		super();
		this.kapacitorServiceUrl = kapacitorServiceUrl;
	}

	private String excuteGet(String option) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(kapacitorServiceUrl + option);
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				HttpEntity entity = response.getEntity();
				String resStr = EntityUtils.toString(entity);
				LOGGER.debug("===kapacitor请求地址===" + httpget.getURI() + "===返回响应===" + resStr);
				return resStr;
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static final String APPLICATION_JSON = "application/json";

	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

	private String excutePost(String option, String json) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = new HttpPost(kapacitorServiceUrl + option);
			// 将JSON进行UTF-8编码,以便传输中文
			//String encoderJson = URLEncoder.encode(json, HTTP.UTF_8);
			httppost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			StringEntity se = new StringEntity(json);
			se.setContentType(CONTENT_TYPE_TEXT_JSON);
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
			httppost.setEntity(se);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				String resStr = EntityUtils.toString(entity);
				LOGGER.debug("===kapacitor请求地址===" + httppost.getURI() + "===返回响应===" + resStr);
				return resStr;
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private int excuteDelete(String option) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpDelete httpdelete = new HttpDelete(kapacitorServiceUrl + option);
			CloseableHttpResponse response = httpclient.execute(httpdelete);
			return response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	private String excutePatch(String option, String json) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPatch httppatch = new HttpPatch(kapacitorServiceUrl + option);
			// 将JSON进行UTF-8编码,以便传输中文
			//String encoderJson = URLEncoder.encode(json, HTTP.UTF_8);
			httppatch.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			StringEntity se = new StringEntity(json);
			se.setContentType(CONTENT_TYPE_TEXT_JSON);
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
			httppatch.setEntity(se);
			CloseableHttpResponse response = httpclient.execute(httppatch);
			try {
				HttpEntity entity = response.getEntity();
				String resStr = EntityUtils.toString(entity);
				LOGGER.debug("===kapacitor请求地址===" + httppatch.getURI() + "===返回响应===" + resStr);
				return resStr;
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getTaskList() {
		return excuteGet("/tasks");
	}
	
	public String createTask(String id,String type,String dbrps,String script,String status) {
		JSONObject params = new JSONObject();
		params.put("id", id);
		params.put("type", type);
		JSONArray dbrpsparams = JSONArray.parseArray(dbrps);
		params.put("dbrps", dbrpsparams);
		params.put("script", script);
		if(!"".equals(status) && status != null){
			params.put("status", status);
		}
		return excutePost("/tasks",params.toJSONString());
	}
	
	public void deleteTask(String id) {
		excuteDelete("/tasks/"+id);
	}
	
	public void enableTask(String id) {
		JSONObject params = new JSONObject();
		params.put("status", "enabled");
		excutePatch("/tasks/"+id,params.toJSONString());
	}
	
	public void disableTask(String id) {
		JSONObject params = new JSONObject();
		params.put("status", "disabled");
		excutePatch("/tasks/"+id,params.toJSONString());
	}
}
