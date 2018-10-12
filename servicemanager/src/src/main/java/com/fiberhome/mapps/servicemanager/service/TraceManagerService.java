package com.fiberhome.mapps.servicemanager.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.fiberhome.mapps.servicemanager.entity.TraceInfo;
import com.fiberhome.mapps.servicemanager.request.IdRequest;
import com.fiberhome.mapps.servicemanager.request.TraceDependancyRequest;
import com.fiberhome.mapps.servicemanager.request.TraceListRequest;
import com.fiberhome.mapps.servicemanager.response.JSONArrayResponse;
import com.fiberhome.mapps.servicemanager.response.TraceInfoListResponse;
import com.fiberhome.mapps.servicemanager.utils.ErrorCode;
import com.fiberhome.mapps.servicemanager.utils.LogUtil;
import com.rop.AbstractRopRequest;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean
public class TraceManagerService {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Value("${spring.zipkin.baseUrl}")
	String zipkinBaseUrl;

	String zipkinApiVersion = "v1";

	String zipkinServiceUrl = "/zipkin/api/" + zipkinApiVersion;

	private RestTemplate restTemplate;

	@ServiceMethod(method = "mapps.servicemanager.trace.servicelist", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public JSONArrayResponse getServiceList(AbstractRopRequest req) {
		LOGGER.debug("===获取调用服务列表接口(mapps.servicemanager.trace.servicelist)入口===");
		JSONArrayResponse response = new JSONArrayResponse();
		try {
			String reqUrl = zipkinBaseUrl + zipkinServiceUrl + "/services";
			restTemplate = new RestTemplate();
			String rs = restTemplate.getForObject(reqUrl, String.class);
			JSONArray jsonArray = JSONArray.parseArray(rs);
			response.setJsonArray(jsonArray);
			LOGGER.debug("获取调用服务列表成功");
		} catch (Exception e) {
			LOGGER.error("获取调用服务列表异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.trace.getspans", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public JSONArrayResponse getSpanList(IdRequest req) {
		LOGGER.debug("===获取调用请求列表接口(mapps.servicemanager.trace.getspans)入口,请求参数===" + LogUtil.getObjectInfo(req));
		JSONArrayResponse response = new JSONArrayResponse();
		try {
			String reqUrl = zipkinBaseUrl + zipkinServiceUrl + "/spans?serviceName=" + req.getId();
			restTemplate = new RestTemplate();
			String rs = restTemplate.getForObject(reqUrl, String.class);
			JSONArray jsonArray = JSONArray.parseArray(rs);
			response.setJsonArray(jsonArray);
			LOGGER.debug("获取调用请求列表成功");
		} catch (Exception e) {
			LOGGER.error("获取调用请求列表异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.trace.gettraceList", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public TraceInfoListResponse getTraceList(TraceListRequest req) {
		LOGGER.debug("===获取链路跟踪列表接口(mapps.servicemanager.trace.gettraceList)入口,请求参数===" + LogUtil.getObjectInfo(req));
		TraceInfoListResponse response = new TraceInfoListResponse();
		try {
			String reqUrl = zipkinBaseUrl + zipkinServiceUrl + "/traces?annotationQuery=" + req.getAnnotationQuery()
					+ "&endTs=" + req.getEndTs() + "&limit=" + req.getTracelimit() + "&lookback=" + req.getLookback()
					+ "&minDuration=" + req.getMinDuration() + "&serviceName=" + req.getServiceName() + "&spanName="
					+ req.getSpanName();// + "&sortOrder=" + req.getSortOrder()
			restTemplate = new RestTemplate();
			String rs = restTemplate.getForObject(reqUrl, String.class);
			JSONArray jsonArray = JSONArray.parseArray(rs);
			List<TraceInfo> traceInfoList = new ArrayList<TraceInfo>();
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONArray oneTrace = jsonArray.getJSONArray(i);
				TraceInfo ti = new TraceInfo();
				String traceId = oneTrace.getJSONObject(0).getString("traceId");
				long duration = oneTrace.getJSONObject(0).getLong("duration");
				long timestamp = oneTrace.getJSONObject(0).getLong("timestamp");
				//计算duration
				long maxEndTimestamp = timestamp + duration;
				long minStartTimestamp = timestamp;
				for (int j = 1; j < oneTrace.size(); j++) {
					long oneEndTimestamp = oneTrace.getJSONObject(j).getLong("timestamp") + oneTrace.getJSONObject(j).getLong("duration");
					if(maxEndTimestamp < oneEndTimestamp){
						maxEndTimestamp = oneEndTimestamp;
					}
					if(minStartTimestamp > oneTrace.getJSONObject(j).getLong("timestamp")){
						minStartTimestamp = oneTrace.getJSONObject(j).getLong("timestamp");
					}
				}
				duration = maxEndTimestamp - minStartTimestamp;
				JSONArray zeroAnnotations = oneTrace.getJSONObject(0).getJSONArray("annotations");
				Date datetime = new Date(timestamp / 1000);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = formatter.format(datetime);
				boolean isFullPercent = false;
				String percent = "";
				double per = 100;
				if (zeroAnnotations == null) {
					percent = "100%";
				} else {
					for (int m = 0; m < zeroAnnotations.size(); m++) {
						if (zeroAnnotations.getJSONObject(m).getJSONObject("endpoint").getString("serviceName")
								.contains(req.getServiceName())) {
							isFullPercent = true;
						}
					}
					if (isFullPercent) {
						percent = "100%";
					} else {
						long selfduration = 0;
						long parentStartTimestamp = 0;
						long parentEndTimestamp = 0;
						for (int j = 1; j < oneTrace.size(); j++) {
							JSONArray annotations = oneTrace.getJSONObject(j).getJSONArray("annotations");
							if (annotations != null) {
								boolean isAddPercent = false;
								for (int m = 0; m < annotations.size(); m++) {
									if (annotations.getJSONObject(m).getJSONObject("endpoint").getString("serviceName")
											.contains(req.getServiceName())) {
										isAddPercent = true;
									}
								}
								if (isAddPercent) {
									long curStartTimestamp = oneTrace.getJSONObject(j).getLong("timestamp");
									long curEndTimestamp = oneTrace.getJSONObject(j).getLong("duration")+oneTrace.getJSONObject(j).getLong("timestamp");
									if(selfduration == 0){
										selfduration += oneTrace.getJSONObject(j).getLong("duration");
									}else{
										if((parentEndTimestamp < curEndTimestamp) || (parentStartTimestamp > curStartTimestamp)){
											selfduration += oneTrace.getJSONObject(j).getLong("duration");
										}
									}
									parentStartTimestamp = curStartTimestamp;
									parentEndTimestamp = curEndTimestamp;
								}
							}
						}
						per = ((double) selfduration / (double) duration) * 100;
						percent = (int) Math.floor(per) + "%";
					}
				}
				ti.setTraceId(traceId);
				ti.setDuration(duration);
				ti.setSpans(oneTrace.size());
				ti.setPercent(percent);
				ti.setPer(per);
				ti.setTime(time);
				ti.setTimestamp(timestamp);
				traceInfoList.add(ti);
			}
			// 排序
			if ("duration asc".equals(req.getSort())) {
				Collections.sort(traceInfoList, new SortByDurationAsc());
			} else if ("duration desc".equals(req.getSort())) {
				Collections.sort(traceInfoList, new SortByDurationDesc());
			} else if ("percent asc".equals(req.getSort())) {
				Collections.sort(traceInfoList, new SortByPercentAsc());
			} else if ("percent desc".equals(req.getSort())) {
				Collections.sort(traceInfoList, new SortByPercentDesc());
			} else if ("time asc".equals(req.getSort())) {
				Collections.sort(traceInfoList, new SortByTimeAsc());
			} else if ("time desc".equals(req.getSort())) {
				Collections.sort(traceInfoList, new SortByTimeDesc());
			}

			// 分页
			List<TraceInfo> subList = traceInfoList.subList((req.getOffset() - 1) * req.getLimit(),
					(req.getLimit() * req.getOffset() > traceInfoList.size()) ? traceInfoList.size()
							: (req.getLimit() * req.getOffset()));
			response.setTotal(traceInfoList.size());
			response.setTraceInfoList(subList);
			LOGGER.debug("获取链路跟踪列表成功");
		} catch (Exception e) {
			LOGGER.error("获取链路跟踪列表异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	class SortByDurationDesc implements Comparator<TraceInfo> {
		public int compare(TraceInfo s1, TraceInfo s2) {
			if (s1.getDuration() == s2.getDuration()) {
				return 0;
			} else {
				return (s1.getDuration() > s2.getDuration()) ? -1 : 1;
			}
		}
	}

	class SortByDurationAsc implements Comparator<TraceInfo> {
		public int compare(TraceInfo s1, TraceInfo s2) {
			if (s1.getDuration() == s2.getDuration()) {
				return 0;
			} else {
				return (s2.getDuration() > s1.getDuration()) ? -1 : 1;
			}
		}
	}

	class SortByTimeDesc implements Comparator<TraceInfo> {
		public int compare(TraceInfo s1, TraceInfo s2) {
			if (s1.getTimestamp() == s2.getTimestamp()) {
				return 0;
			} else {
				return (s1.getTimestamp() > s2.getTimestamp()) ? -1 : 1;
			}
		}
	}

	class SortByTimeAsc implements Comparator<TraceInfo> {
		public int compare(TraceInfo s1, TraceInfo s2) {
			if (s1.getTimestamp() == s2.getTimestamp()) {
				return 0;
			} else {
				return (s2.getTimestamp() > s1.getTimestamp()) ? -1 : 1;
			}
		}
	}

	class SortByPercentDesc implements Comparator<TraceInfo> {
		public int compare(TraceInfo s1, TraceInfo s2) {
			if (s1.getPer() == s2.getPer()) {
				return 0;
			} else {
				return (s1.getPer() > s2.getPer()) ? -1 : 1;
			}
		}
	}

	class SortByPercentAsc implements Comparator<TraceInfo> {
		public int compare(TraceInfo s1, TraceInfo s2) {
			if (s1.getPer() == s2.getPer()) {
				return 0;
			} else {
				return (s2.getPer() > s1.getPer()) ? -1 : 1;
			}
		}
	}

	@ServiceMethod(method = "mapps.servicemanager.trace.detail", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public JSONArrayResponse getDetail(IdRequest req) {
		LOGGER.debug("===获取调用详情接口(mapps.servicemanager.trace.detail)入口,请求参数===" + LogUtil.getObjectInfo(req));
		JSONArrayResponse response = new JSONArrayResponse();
		try {
			String reqUrl = zipkinBaseUrl + zipkinServiceUrl + "/trace/" + req.getId();
			restTemplate = new RestTemplate();
			String rs = restTemplate.getForObject(reqUrl, String.class);
			JSONArray jsonArray = JSONArray.parseArray(rs);
			response.setJsonArray(jsonArray);
			LOGGER.debug("获取调用详情成功");
		} catch (Exception e) {
			LOGGER.error("获取调用详情异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.trace.dependancy", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public JSONArrayResponse getDependency(TraceDependancyRequest req) {
		LOGGER.debug("===获取调用依赖接口(mapps.servicemanager.trace.dependancy)入口,请求参数===" + LogUtil.getObjectInfo(req));
		JSONArrayResponse response = new JSONArrayResponse();
		try {
			String reqUrl = zipkinBaseUrl + zipkinServiceUrl + "/dependencies?endTs="+req.getEndTs()+"&lookback=" + req.getLookback();
			restTemplate = new RestTemplate();
			String rs = restTemplate.getForObject(reqUrl, String.class);
			JSONArray jsonArray = JSONArray.parseArray(rs);
			response.setJsonArray(jsonArray);
			LOGGER.debug("获取调用依赖成功");
		} catch (Exception e) {
			LOGGER.error("获取调用依赖异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
}
