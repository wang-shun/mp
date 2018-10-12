package com.fiberhome.mapps.servicemanager.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.dao.McAlertConditionMapper;
import com.fiberhome.mapps.servicemanager.dao.McAlertLogsMapper;
import com.fiberhome.mapps.servicemanager.dao.McAlertMethodMapper;
import com.fiberhome.mapps.servicemanager.dao.McAlertRuleMapper;
import com.fiberhome.mapps.servicemanager.dao.McDownsampleMapper;
import com.fiberhome.mapps.servicemanager.dao.McMeasurementMapper;
import com.fiberhome.mapps.servicemanager.dao.McRetentionPolicyMapper;
import com.fiberhome.mapps.servicemanager.dao.McSystemMapper;
import com.fiberhome.mapps.servicemanager.dao.McTagMapper;
import com.fiberhome.mapps.servicemanager.dao.McUnitWeightMapper;
import com.fiberhome.mapps.servicemanager.dao.McValueFieldMapper;
import com.fiberhome.mapps.servicemanager.entity.ClientAlertlogInfo;
import com.fiberhome.mapps.servicemanager.entity.McAlertCondition;
import com.fiberhome.mapps.servicemanager.entity.McAlertLogs;
import com.fiberhome.mapps.servicemanager.entity.McAlertMethod;
import com.fiberhome.mapps.servicemanager.entity.McAlertRule;
import com.fiberhome.mapps.servicemanager.entity.McDownsample;
import com.fiberhome.mapps.servicemanager.entity.McMeasurement;
import com.fiberhome.mapps.servicemanager.entity.McRetentionPolicy;
import com.fiberhome.mapps.servicemanager.entity.McSystem;
import com.fiberhome.mapps.servicemanager.entity.McTag;
import com.fiberhome.mapps.servicemanager.entity.McUnitWeight;
import com.fiberhome.mapps.servicemanager.entity.McValueField;
import com.fiberhome.mapps.servicemanager.entity.RsKeyValue;
import com.fiberhome.mapps.servicemanager.influxdb.InfluxdbUtil;
import com.fiberhome.mapps.servicemanager.kapacitor.KapacitorUtil;
import com.fiberhome.mapps.servicemanager.kapacitor.TickScriptTemplate;
import com.fiberhome.mapps.servicemanager.request.FieldValueSaveRequest;
import com.fiberhome.mapps.servicemanager.request.IdRequest;
import com.fiberhome.mapps.servicemanager.request.InfluxSqlRequest;
import com.fiberhome.mapps.servicemanager.request.MeasurementSaveRequest;
import com.fiberhome.mapps.servicemanager.request.QueryListRequest;
import com.fiberhome.mapps.servicemanager.request.RetentionSaveRequest;
import com.fiberhome.mapps.servicemanager.request.RuleSaveRequest;
import com.fiberhome.mapps.servicemanager.request.SampleSaveRequest;
import com.fiberhome.mapps.servicemanager.request.TagSaveRequest;
import com.fiberhome.mapps.servicemanager.response.AlertlogsQueryResponse;
import com.fiberhome.mapps.servicemanager.response.FieldValueListResponse;
import com.fiberhome.mapps.servicemanager.response.InfluxdbSeriesResponse;
import com.fiberhome.mapps.servicemanager.response.KapcitorFilterResponse;
import com.fiberhome.mapps.servicemanager.response.MeasurementDetailResponse;
import com.fiberhome.mapps.servicemanager.response.MeasurementQueryResponse;
import com.fiberhome.mapps.servicemanager.response.QueryResultResponse;
import com.fiberhome.mapps.servicemanager.response.RetentionDetailResponse;
import com.fiberhome.mapps.servicemanager.response.RetentionQueryResponse;
import com.fiberhome.mapps.servicemanager.response.RuleDetailResponse;
import com.fiberhome.mapps.servicemanager.response.RuleQueryResponse;
import com.fiberhome.mapps.servicemanager.response.SampleDetailResponse;
import com.fiberhome.mapps.servicemanager.response.SampleQueryResponse;
import com.fiberhome.mapps.servicemanager.response.TagQueryResponse;
import com.fiberhome.mapps.servicemanager.response.UnitWeightListResponse;
import com.fiberhome.mapps.servicemanager.utils.ErrorCode;
import com.fiberhome.mapps.servicemanager.utils.IDGen;
import com.fiberhome.mapps.servicemanager.utils.JsEscape;
import com.fiberhome.mapps.servicemanager.utils.JsonUtil;
import com.fiberhome.mapps.servicemanager.utils.LogUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.rop.AbstractRopRequest;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean
public class MonitorManagerService {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Value("${influxdb.serviceUrl:}")
	private String influxdbServiceUrl;

	@Value("${influxdb.adminUser:}")
	private String adminUser;

	@Value("${influxdb.adminPass:}")
	private String adminPass;

	@Value("${kapacitor.serviceUrl:}")
	private String kapacitorServiceUrl;

	@Value("${kapacitor.osName}")
	String kapacitorOsName;
	
	@Value("${sms.key}")
	String smsPushKey;

	@Autowired
	McSystemMapper systemMapper;

	@Autowired
	McMeasurementMapper measurementMapper;

	@Autowired
	McValueFieldMapper valueFieldMapper;

	@Autowired
	McUnitWeightMapper unitWeightMapper;

	@Autowired
	McTagMapper tagMapper;

	@Autowired
	McDownsampleMapper downsampleMapper;

	@Autowired
	McRetentionPolicyMapper retentionPolicyMapper;

	@Autowired
	McAlertRuleMapper alertRuleMapper;

	@Autowired
	McAlertMethodMapper alertMethodMapper;

	@Autowired
	McAlertConditionMapper alertConditionMapper;
	
	@Autowired
	McAlertLogsMapper alertLogsMapper;

	@Autowired
	McSystemService mss;

	@ServiceMethod(method = "mapps.servicemanager.measurement.query", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public MeasurementQueryResponse queryMeasurementList(QueryListRequest req) {
		LOGGER.debug("===获取展示指标列表接口(mapps.servicemanager.measurement.query)入口===");
		MeasurementQueryResponse response = new MeasurementQueryResponse();
		Map<String, Object> map = new HashMap<String, Object>();
		McSystem ms = mss.getCurrentSystem();
		map.put("system_id", ms.getId());
		// if (req.getIsenabled() != null) {
		// map.put("isenabled", req.getIsenabled());
		// }
		if (req.getKeyword() != null) {
			map.put("keyword", "%" + req.getKeyword() + "%");
		}
		if (StringUtil.isNotEmpty(req.getSort())) {
			String sort = "";
			if (req.getSort().contains("resId")) {
				sort = req.getSort().replaceAll("resId", "res_id");
			} else {
				sort = req.getSort();
			}
			map.put("sort", sort);
		}
		PageHelper.startPage(req.getOffset(), req.getLimit());
		List<McMeasurement> list = measurementMapper.getMeasurementList(map);
		PageInfo<McMeasurement> page = new PageInfo<McMeasurement>(list);
		response.setTotal(page.getTotal());
		if (list == null) {
			list = new ArrayList<McMeasurement>();
		}
		response.setMeasurementList(list);
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.measurement.list", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public QueryResultResponse getMeasurementList(AbstractRopRequest req) {
		LOGGER.debug("===获取influxdb指标列表接口(mapps.servicemanager.measurement.list)===");
		QueryResultResponse response = new QueryResultResponse();
		InfluxdbUtil influxdb = null;
		try {
			McSystem ms = mss.getCurrentSystem();
			influxdb = new InfluxdbUtil(influxdbServiceUrl, ms.getDb(), adminUser, adminPass);
			List<Result> measurementList = influxdb.query("SHOW MEASUREMENTS");
			response.setQueryResult(measurementList);
			LOGGER.debug("获取influxdb指标列表成功");
		} catch (Exception e) {
			LOGGER.error("获取influxdb指标列表异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		} finally {
			if (influxdb != null) {
				influxdb.close();
			}
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.measurement.detail", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public MeasurementDetailResponse getMeasurementDetail(IdRequest req) {
		LOGGER.debug("===获取指标详情接口(mapps.servicemanager.measurement.detail),请求参数===" + LogUtil.getObjectInfo(req));
		MeasurementDetailResponse response = new MeasurementDetailResponse();
		try {
			McMeasurement mm = new McMeasurement();
			mm.setId(req.getId());
			McMeasurement mmlt = measurementMapper.selectOne(mm);
			response.setMeasurementDetail(mmlt);
			LOGGER.debug("获取指标详情成功");
		} catch (Exception e) {
			LOGGER.error("获取指标详情异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.measurement.add", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse addMeasurement(MeasurementSaveRequest req) {
		LOGGER.debug("===新增指标接口(mapps.servicemanager.measurement.add),请求参数===" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			String id = IDGen.uuid().replaceAll("-", "");
			McMeasurement mm = new McMeasurement();
			mm.setMeasurement(req.getMeasurement());
			List<McMeasurement> mmlt = measurementMapper.select(mm);
			if (mmlt.size() > 0) {
				ErrorCode.fail(response, ErrorCode.CODE_300053);
			} else {
				mm.setId(id);
				mm.setSystemId(mss.getCurrentSystem().getId());
				mm.setName(JsEscape.unescape(req.getName()));
				if (!"".equals(req.getRetainTime())) {
					mm.setRetainTime(req.getRetainTime() + req.getRetainTimeUnit());
				}
				measurementMapper.insertSelective(mm);
				LOGGER.debug("新增指标成功");
			}
		} catch (Exception e) {
			LOGGER.error("新增指标异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.measurement.edit", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse editMeasurement(MeasurementSaveRequest req) {
		LOGGER.debug("===保存指标接口(mapps.servicemanager.measurement.add),请求参数===" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			McMeasurement mm = new McMeasurement();
			mm.setId(req.getId());
			mm.setSystemId(mss.getCurrentSystem().getId());
			mm.setMeasurement(req.getMeasurement());
			mm.setName(JsEscape.unescape(req.getName()));
			if (!"".equals(req.getRetainTime())) {
				mm.setRetainTime(req.getRetainTime() + req.getRetainTimeUnit());
			} else {
				mm.setRetainTime("");
			}
			measurementMapper.updateByPrimaryKeySelective(mm);
			LOGGER.debug("保存指标成功");
		} catch (Exception e) {
			LOGGER.error("保存指标异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.field.list", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public FieldValueListResponse getMeasurementValueList(IdRequest req) {
		LOGGER.debug("===获取值域列表接口(mapps.servicemanager.field.list),请求参数===" + LogUtil.getObjectInfo(req));
		FieldValueListResponse response = new FieldValueListResponse();
		try {
			McValueField mvf = new McValueField();
			mvf.setMeasurementId(req.getId());
			List<McValueField> list = valueFieldMapper.select(mvf);
			Collections.sort(list, new SortByField());
			response.setList(list);
			LOGGER.debug("获取值域列表成功");
		} catch (Exception e) {
			LOGGER.error("获取值域列表异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	class SortByField implements Comparator<McValueField> {
		public int compare(McValueField s1, McValueField s2) {
			return s1.getField().compareTo(s2.getField());
		}
	}

	@ServiceMethod(method = "mapps.servicemanager.field.value", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public QueryResultResponse getMeasurementValue(IdRequest req) {
		LOGGER.debug("===获取值域接口(mapps.servicemanager.field.value),请求参数===" + LogUtil.getObjectInfo(req));
		QueryResultResponse response = new QueryResultResponse();
		InfluxdbUtil influxdb = null;
		try {
			McSystem ms = mss.getCurrentSystem();
			influxdb = new InfluxdbUtil(influxdbServiceUrl, ms.getDb(), adminUser, adminPass);
			McMeasurement mm = measurementMapper.selectByPrimaryKey(req.getId());
			List<Result> queryResult = influxdb.query("show field keys from \"" + mm.getMeasurement() + "\"");
			response.setQueryResult(queryResult);
			LOGGER.debug("获取值域成功");
		} catch (Exception e) {
			LOGGER.error("获取值域异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		} finally {
			if (influxdb != null) {
				influxdb.close();
			}
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.field.add", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse addFieldValue(FieldValueSaveRequest req) {
		LOGGER.debug("===新增值域接口(mapps.servicemanager.field.add),请求参数===" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			String id = IDGen.uuid().replaceAll("-", "");
			McValueField mvf = new McValueField();
			mvf.setField(req.getField());
			mvf.setMeasurementId(req.getMeasurementId());
			List<McValueField> mvflt = valueFieldMapper.select(mvf);
			if (mvflt.size() > 0) {
				ErrorCode.fail(response, ErrorCode.CODE_300050);
			} else {
				mvf.setId(id);
				mvf.setMeasurementId(req.getMeasurementId());
				mvf.setName(JsEscape.unescape(req.getName()));
				mvf.setUnit(JsEscape.unescape(req.getUnit()));
				valueFieldMapper.insertSelective(mvf);
				LOGGER.debug("新增值域成功");
			}
		} catch (Exception e) {
			LOGGER.error("新增值域异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.field.edit", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse editFieldValue(FieldValueSaveRequest req) {
		LOGGER.debug("===编辑值域接口(mapps.servicemanager.field.add),请求参数===" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			String id = req.getId();
			McValueField mvf = new McValueField();
			mvf.setField(req.getField());
			mvf.setId(id);
			mvf.setName(JsEscape.unescape(req.getName()));
			mvf.setUnit(JsEscape.unescape(req.getUnit()));
			valueFieldMapper.updateByPrimaryKeySelective(mvf);
			LOGGER.debug("编辑值域成功");
		} catch (Exception e) {
			LOGGER.error("编辑值域异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.field.delete", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse deleteFieldValue(IdRequest req) {
		LOGGER.debug("===删除值域接口(mapps.servicemanager.field.delete),请求参数===" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			valueFieldMapper.deleteByPrimaryKey(req.getId());
			LOGGER.debug("删除值域成功");
		} catch (Exception e) {
			LOGGER.error("删除值域异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.unit.get", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public UnitWeightListResponse getUnitWeight(AbstractRopRequest req) {
		LOGGER.debug("===获取单位接口(mapps.servicemanager.unit.get)===");
		UnitWeightListResponse response = new UnitWeightListResponse();
		try {
			List<McUnitWeight> list = unitWeightMapper.selectAll();
			response.setUnitList(list);
			LOGGER.debug("获取单位成功");
		} catch (Exception e) {
			LOGGER.error("获取单位异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.tag.query", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public TagQueryResponse queryTagList(QueryListRequest req) {
		LOGGER.debug("===获取展示tag列表接口(mapps.servicemanager.tag.query)入口,请求参数===" + LogUtil.getObjectInfo(req));
		TagQueryResponse response = new TagQueryResponse();
		List<String> tagStrList = new ArrayList<String>();
		List<McTag> tagList = new ArrayList<McTag>();
		String tagDuplicateList = "";
		InfluxdbUtil influxdb = null;
		try {
			McSystem ms = mss.getCurrentSystem();
			influxdb = new InfluxdbUtil(influxdbServiceUrl, ms.getDb(), adminUser, adminPass);
			List<Result> measurementList = influxdb.query("SHOW TAG KEYS");
			if(measurementList.get(0).getSeries() != null){
				for (Series s : measurementList.get(0).getSeries()) {
					for (List<Object> values : s.getValues()) {
						if (tagDuplicateList.indexOf((String) values.get(0) + ",") < 0) {
							tagStrList.add((String) values.get(0));
							tagDuplicateList += (String) values.get(0) + ",";
						}
					}
				}
			}
			List<McTag> tagAllList = tagMapper.selectAll();
			for (String s : tagStrList) {
				McTag mt = new McTag();
				mt.setTag(s);
				mt.setSystemId(ms.getId());
				for (McTag t : tagAllList) {
					if (s.equals(t.getTag()) && ms.getId().equals(t.getSystemId())) {
						mt.setId(t.getId());
						mt.setName(t.getName());
					}
				}
				if (req.getKeyword() != null) {
					if (mt.getTag().indexOf(req.getKeyword()) > -1) {
						tagList.add(mt);
					}
				} else {
					tagList.add(mt);
				}
			}
			if (req.getSort().indexOf("desc") > -1) {
				Collections.sort(tagList, new SortByTagDesc());
			} else {
				Collections.sort(tagList, new SortByTag());
			}
			List<McTag> tagFinalList = new ArrayList<McTag>();
			int offset = (req.getOffset() - 1) * req.getLimit();
			int pageNum = ((tagList.size() - offset) < req.getLimit()) ? (tagList.size() - offset) : req.getLimit();
			for (int i = 0; i < pageNum; i++) {
				tagFinalList.add(tagList.get((i + offset)));
			}
			response.setTotal(tagList.size());
			response.setTagList(tagFinalList);
			LOGGER.debug("获取展示tag列表成功");
		} catch (Exception e) {
			LOGGER.error("获取展示tag列表异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		} finally {
			if (influxdb != null) {
				influxdb.close();
			}
		}
		return response;
	}

	class SortByTag implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			McTag s1 = (McTag) o1;
			McTag s2 = (McTag) o2;
			return s1.getTag().compareTo(s2.getTag());
		}
	}

	class SortByTagDesc implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			McTag s1 = (McTag) o1;
			McTag s2 = (McTag) o2;
			return s2.getTag().compareTo(s1.getTag());
		}
	}

	@ServiceMethod(method = "mapps.servicemanager.tag.save", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse saveTag(TagSaveRequest req) {
		LOGGER.debug("===保存tag接口(mapps.servicemanager.tag.save),请求参数===" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			McTag mt = new McTag();
			mt.setSystemId(req.getSystemId());
			mt.setTag(req.getTag());
			List<McTag> mtlt = tagMapper.select(mt);
			mt.setName(JsEscape.unescape(req.getName()));
			String id = req.getId();
			if (id != null && !"undefined".equals(id)) {
				mt.setId(id);
				if (!"".equals(mt.getName())) {
					tagMapper.updateByPrimaryKeySelective(mt);
				} else {
					tagMapper.deleteByPrimaryKey(id);
				}
			} else {
				id = IDGen.uuid().replaceAll("-", "");
				mt.setId(id);
				if (!"".equals(mt.getName()) && !"undefined".equals(mt.getName()) && mtlt.size() < 1) {
					tagMapper.insertSelective(mt);
				}
			}
			LOGGER.debug("保存tag成功");
		} catch (Exception e) {
			LOGGER.error("保存tag异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.sample.query", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public SampleQueryResponse querySampleList(QueryListRequest req) {
		LOGGER.debug("===获取采样设置列表接口(mapps.servicemanager.sample.query)入口===");
		SampleQueryResponse response = new SampleQueryResponse();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if (req.getIsenabled() != null) {
				map.put("isenabled", req.getIsenabled());
			}
			if (req.getKeyword() != null) {
				map.put("keyword", "%" + req.getKeyword() + "%");
			}
			if (StringUtil.isNotEmpty(req.getSort())) {
				String sort = "";
				if (req.getSort().contains("resId")) {
					sort = req.getSort().replaceAll("resId", "res_id");
				} else {
					sort = req.getSort();
				}
				map.put("sort", sort);
			}
			PageHelper.startPage(req.getOffset(), req.getLimit());
			List<McDownsample> list = downsampleMapper.getQueryList(map);
			PageInfo<McDownsample> page = new PageInfo<McDownsample>(list);
			response.setTotal(page.getTotal());
			if (list == null) {
				list = new ArrayList<McDownsample>();
			}
			response.setSampleList(list);
			LOGGER.debug("获取采样设置列表成功");
		} catch (Exception e) {
			LOGGER.error("获取采样设置列表异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.sample.disable", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse stopRedis(IdRequest req) {
		LOGGER.debug("===禁用采样设置接口(mapps.servicemanager.sample.disable)===");
		BaseResponse response = new BaseResponse();
		InfluxdbUtil influxdb = null;
		try {
			McDownsample mds = new McDownsample();
			mds.setId(req.getId());
			mds.setEnabled("0");
			downsampleMapper.updateByPrimaryKeySelective(mds);
			String id = req.getId();
			// 删除原有continuous query
			McSystem ms = mss.getCurrentSystem();
			influxdb = new InfluxdbUtil(influxdbServiceUrl, ms.getDb(), adminUser, adminPass);
			McDownsample mdsForDel = downsampleMapper.selectByPrimaryKey(id);
			influxdb.dropContinuousQuery(mdsForDel.getCqName());
			LOGGER.debug("禁用采样设置成功");
		} catch (Exception e) {
			LOGGER.error("禁用采样设置异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		} finally {
			if (influxdb != null) {
				influxdb.close();
			}
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.sample.enable", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse startRedis(IdRequest req) {
		LOGGER.debug("===启用采样设置接口(mapps.servicemanager.sample.enable)===");
		BaseResponse response = new BaseResponse();
		InfluxdbUtil influxdb = null;
		try {
			McDownsample mds = new McDownsample();
			mds.setId(req.getId());
			mds.setEnabled("1");
			downsampleMapper.updateByPrimaryKeySelective(mds);
			// 写入influxdb
			McSystem ms = mss.getCurrentSystem();
			influxdb = new InfluxdbUtil(influxdbServiceUrl, ms.getDb(), adminUser, adminPass);
			McDownsample mdsForCreate = downsampleMapper.selectByPrimaryKey(req.getId());
			influxdb.createContinuousQuery(mdsForCreate.getCqName(), mdsForCreate.getSampleSql());
			LOGGER.debug("启用采样设置成功");
		} catch (Exception e) {
			LOGGER.error("启用采样设置异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.sample.add", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse addSample(SampleSaveRequest req) {
		LOGGER.debug("===新增采样设置接口(mapps.servicemanager.sample.add),请求参数===" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		InfluxdbUtil influxdb = null;
		try {
			String id = IDGen.uuid().replaceAll("-", "");
			McDownsample mds = new McDownsample();
			mds.setCqName(JsEscape.unescape(req.getMeasurement()));
			List<McDownsample> mdslt = downsampleMapper.select(mds);
			if (mdslt.size() > 0) {
				ErrorCode.fail(response, ErrorCode.CODE_300051);
				return response;
			} else {
				mds.setId(id);
				mds.setSystemId(mss.getCurrentSystem().getId());
				mds.setRemarks(JsEscape.unescape(req.getRemarks()));
				mds.setSampleSql(JsEscape.unescape(req.getSampleSql()));
				mds.setSynced("0");
				mds.setEnabled("1");

				// 写入influxdb
				McSystem ms = mss.getCurrentSystem();
				influxdb = new InfluxdbUtil(influxdbServiceUrl, ms.getDb(), adminUser, adminPass);
				influxdb.createContinuousQuery(mds.getCqName(), mds.getSampleSql());

				downsampleMapper.insertSelective(mds);
				LOGGER.debug("新增采样设置成功");
			}
		} catch (Exception e) {
			LOGGER.error("新增采样设置异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
			String errMsg = e.getMessage();
			if(errMsg.indexOf("error parsing query") > -1){
				response.setError_message("输入的influxdb sql语句不符合规则");
			}
			return response;
		} finally {
			if (influxdb != null) {
				influxdb.close();
			}
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.sample.edit", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse editSample(SampleSaveRequest req) {
		LOGGER.debug("===编辑采样设置接口(mapps.servicemanager.sample.edit),请求参数===" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		InfluxdbUtil influxdb = null;
		try {
			String id = req.getId();
			// 删除原有continuous query
			McSystem ms = mss.getCurrentSystem();
			influxdb = new InfluxdbUtil(influxdbServiceUrl, ms.getDb(), adminUser, adminPass);
			McDownsample mdsForDel = downsampleMapper.selectByPrimaryKey(id);
			influxdb.dropContinuousQuery(mdsForDel.getCqName());

			McDownsample mds = new McDownsample();
			mds.setId(id);
			mds.setCqName(JsEscape.unescape(req.getMeasurement()));
			mds.setRemarks(JsEscape.unescape(req.getRemarks()));
			mds.setSampleSql(JsEscape.unescape(req.getSampleSql()));

			// 写入influxdb
			influxdb.createContinuousQuery(mds.getCqName(), mds.getSampleSql());
			
			downsampleMapper.updateByPrimaryKeySelective(mds);
			LOGGER.debug("编辑采样设置成功");
		} catch (Exception e) {
			LOGGER.error("编辑采样设置异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
			String errMsg = e.getMessage();
			if(errMsg.indexOf("error parsing query") > -1){
				response.setError_message("输入的influxdb sql语句不符合规则");
			}
			return response;
		} finally {
			if (influxdb != null) {
				influxdb.close();
			}
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.sample.detail", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public SampleDetailResponse getSampleDetail(IdRequest req) {
		LOGGER.debug("===获取采样设置详情接口(mapps.servicemanager.sample.detail),请求参数===" + LogUtil.getObjectInfo(req));
		SampleDetailResponse response = new SampleDetailResponse();
		try {
			McDownsample mds = new McDownsample();
			mds.setId(req.getId());
			McDownsample mdslt = downsampleMapper.selectOne(mds);
			response.setSampleDetail(mdslt);
			LOGGER.debug("获取采样设置详情成功");
		} catch (Exception e) {
			LOGGER.error("获取采样设置详情异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.retention.query", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RetentionQueryResponse queryRetentionList(QueryListRequest req) {
		LOGGER.debug("===获取保留策略列表接口(mapps.servicemanager.retention.query)入口===");
		RetentionQueryResponse response = new RetentionQueryResponse();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if (req.getIsenabled() != null) {
				map.put("isenabled", req.getIsenabled());
			}
			if (req.getKeyword() != null) {
				map.put("keyword", "%" + req.getKeyword() + "%");
			}
			if (StringUtil.isNotEmpty(req.getSort())) {
				String sort = "";
				if (req.getSort().contains("resId")) {
					sort = req.getSort().replaceAll("resId", "res_id");
				} else {
					sort = req.getSort();
				}
				map.put("sort", sort);
			}
			PageHelper.startPage(req.getOffset(), req.getLimit());
			List<McRetentionPolicy> list = retentionPolicyMapper.getQueryList(map);
			PageInfo<McRetentionPolicy> page = new PageInfo<McRetentionPolicy>(list);
			response.setTotal(page.getTotal());
			if (list == null) {
				list = new ArrayList<McRetentionPolicy>();
			}
			response.setRetentionList(list);
			LOGGER.debug("获取保留策略列表成功");
		} catch (Exception e) {
			LOGGER.error("获取保留策略列表异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.retention.add", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse addRetention(RetentionSaveRequest req) {
		LOGGER.debug("===新增保留策略接口(mapps.servicemanager.retention.add),请求参数===" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		InfluxdbUtil influxdb = null;
		try {
			String id = IDGen.uuid().replaceAll("-", "");
			McRetentionPolicy mrp = new McRetentionPolicy();
			mrp.setRp(JsEscape.unescape(req.getRp()));
			List<McRetentionPolicy> mdslt = retentionPolicyMapper.select(mrp);
			if (mdslt.size() > 0) {
				ErrorCode.fail(response, ErrorCode.CODE_300052);
			} else {
				mrp.setId(id);
				mrp.setSystemId(mss.getCurrentSystem().getId());
				mrp.setRpName(JsEscape.unescape(req.getRpName()));
				mrp.setRetainTime(req.getRetainTime());
				mrp.setIsDefault(req.getIsDefault());
				mrp.setEnabled("1");
				if ("1".equals(req.getIsDefault())) {
					McRetentionPolicy mrpdefault = new McRetentionPolicy();
					mrpdefault.setIsDefault("1");
					List<McRetentionPolicy> mrpdefaultlt = retentionPolicyMapper.select(mrpdefault);
					for (McRetentionPolicy mrptemp : mrpdefaultlt) {
						mrptemp.setIsDefault("0");
						retentionPolicyMapper.updateByPrimaryKeySelective(mrptemp);
					}
				}
				retentionPolicyMapper.insertSelective(mrp);

				// 写入influxdb
				McSystem ms = mss.getCurrentSystem();
				influxdb = new InfluxdbUtil(influxdbServiceUrl, ms.getDb(), adminUser, adminPass);
				influxdb.createRetentionPolicy(mrp.getRp(), mrp.getRetainTime(), null, McSystemService.getReplication(),
						false);

				// 将参数应用到缺省策略:autogen
				if ("1".equals(req.getIsDefault())) {
					String duration = "";
					String shardDuration = "";
					List<Result> resultList = influxdb.query("SHOW RETENTION POLICIES ON \"" + ms.getDb() + "\"");
					List<List<Object>> retentionList = resultList.get(0).getSeries().get(0).getValues();
					for (List<Object> retention : retentionList) {
						if (mrp.getRp().equals((String) retention.get(0))) {
							duration = (String) retention.get(1);
							shardDuration = (String) retention.get(2);
						}
					}
					influxdb.excute("ALTER RETENTION POLICY \"autogen\" ON \"" + ms.getDb() + "\" DURATION " + duration
							+ " SHARD DURATION " + shardDuration);
				}
				LOGGER.debug("新增保留策略成功");
			}
		} catch (Exception e) {
			LOGGER.error("新增保留策略异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		} finally {
			if (influxdb != null) {
				influxdb.close();
			}
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.retention.edit", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse editRetention(RetentionSaveRequest req) {
		LOGGER.debug("===新增保留策略接口(mapps.servicemanager.retention.add),请求参数===" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		InfluxdbUtil influxdb = null;
		try {
			String id = req.getId();

			McSystem ms = mss.getCurrentSystem();
			influxdb = new InfluxdbUtil(influxdbServiceUrl, ms.getDb(), adminUser, adminPass);
			// 删除influxdb原有保留策略
			McRetentionPolicy mrpForDel = retentionPolicyMapper.selectByPrimaryKey(id);
			influxdb.dropRetentionPolicy(mrpForDel.getRp());

			McRetentionPolicy mrp = new McRetentionPolicy();
			mrp.setId(id);
			mrp.setRp(JsEscape.unescape(req.getRp()));
			mrp.setRpName(JsEscape.unescape(req.getRpName()));
			mrp.setRetainTime(req.getRetainTime());
			mrp.setIsDefault(req.getIsDefault());
			if ("1".equals(req.getIsDefault())) {
				McRetentionPolicy mrpdefault = new McRetentionPolicy();
				mrpdefault.setIsDefault("1");
				List<McRetentionPolicy> mrpdefaultlt = retentionPolicyMapper.select(mrpdefault);
				for (McRetentionPolicy mrptemp : mrpdefaultlt) {
					mrptemp.setIsDefault("0");
					retentionPolicyMapper.updateByPrimaryKeySelective(mrptemp);
				}
			}
			retentionPolicyMapper.updateByPrimaryKeySelective(mrp);

			// 修改保留策略
			influxdb.createRetentionPolicy(mrp.getRp(), mrp.getRetainTime(), null, McSystemService.getReplication(),
					false);

			// 将参数应用到缺省策略:autogen
			if ("1".equals(req.getIsDefault())) {
				String duration = "";
				String shardDuration = "";
				List<Result> resultList = influxdb.query("SHOW RETENTION POLICIES ON \"" + ms.getDb() + "\"");
				List<List<Object>> retentionList = resultList.get(0).getSeries().get(0).getValues();
				for (List<Object> retention : retentionList) {
					if (mrp.getRp().equals((String) retention.get(0))) {
						duration = (String) retention.get(1);
						shardDuration = (String) retention.get(2);
					}
				}
				influxdb.excute("ALTER RETENTION POLICY \"autogen\" ON \"" + ms.getDb() + "\" DURATION " + duration
						+ " SHARD DURATION " + shardDuration);
			}

			LOGGER.debug("新增保留策略成功");
		} catch (Exception e) {
			LOGGER.error("新增保留策略异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		} finally {
			if (influxdb != null) {
				influxdb.close();
			}
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.retention.delete", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse deleteRetention(IdRequest req) {
		LOGGER.debug("===禁用保留策略接口(mapps.servicemanager.retention.delete)===");
		BaseResponse response = new BaseResponse();
		InfluxdbUtil influxdb = null;
		try {
			// 删除influxdb保留策略
			McSystem ms = mss.getCurrentSystem();
			influxdb = new InfluxdbUtil(influxdbServiceUrl, ms.getDb(), adminUser, adminPass);
			McRetentionPolicy mrpForDel = retentionPolicyMapper.selectByPrimaryKey(req.getId());
			if ("1".equals(mrpForDel.getIsDefault())) {
				ErrorCode.fail(response, ErrorCode.CODE_300054);
			} else {
				influxdb.dropRetentionPolicy(mrpForDel.getRp());
				retentionPolicyMapper.deleteByPrimaryKey(req.getId());
				LOGGER.debug("禁用保留策略成功");
			}
		} catch (Exception e) {
			LOGGER.error("禁用保留策略异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.retention.disable", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse stopRetention(IdRequest req) {
		LOGGER.debug("===禁用保留策略接口(mapps.servicemanager.retention.disable)===");
		BaseResponse response = new BaseResponse();
		try {
			McRetentionPolicy mrp = new McRetentionPolicy();
			mrp.setId(req.getId());
			mrp.setEnabled("0");
			retentionPolicyMapper.updateByPrimaryKeySelective(mrp);
			LOGGER.debug("禁用保留策略成功");
		} catch (Exception e) {
			LOGGER.error("禁用保留策略异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.retention.enable", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse startRetention(IdRequest req) {
		LOGGER.debug("===启用保留策略接口(mapps.servicemanager.retention.enable)===");
		BaseResponse response = new BaseResponse();
		try {
			McRetentionPolicy mrp = new McRetentionPolicy();
			mrp.setId(req.getId());
			mrp.setEnabled("1");
			retentionPolicyMapper.updateByPrimaryKeySelective(mrp);
			LOGGER.debug("启用保留策略成功");
		} catch (Exception e) {
			LOGGER.error("启用保留策略异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.retention.getfrominflux", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public QueryResultResponse getRetentionFromInflux(AbstractRopRequest req) {
		LOGGER.debug("===禁用保留策略接口(mapps.servicemanager.retention.getfrominflux)===");
		QueryResultResponse response = new QueryResultResponse();
		InfluxdbUtil influxdb = null;
		try {
			McSystem ms = mss.getCurrentSystem();
			influxdb = new InfluxdbUtil(influxdbServiceUrl, ms.getDb(), adminUser, adminPass);
			List<Result> queryResult = influxdb.query("SHOW RETENTION POLICIES ON \""+ms.getDb()+"\"");
			response.setQueryResult(queryResult);
			response.setDbName(ms.getDb());
			LOGGER.debug("禁用保留策略成功");
		} catch (Exception e) {
			LOGGER.error("禁用保留策略异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.retention.detail", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RetentionDetailResponse getRetentionDetail(IdRequest req) {
		LOGGER.debug("===获取保留策略详情接口(mapps.servicemanager.retention.detail),请求参数===" + LogUtil.getObjectInfo(req));
		RetentionDetailResponse response = new RetentionDetailResponse();
		try {
			McRetentionPolicy mrp = new McRetentionPolicy();
			mrp.setId(req.getId());
			McRetentionPolicy mrplt = retentionPolicyMapper.selectOne(mrp);
			response.setRetentionPolicyDetail(mrplt);
			LOGGER.debug("获取保留策略详情成功");
		} catch (Exception e) {
			LOGGER.error("获取保留策略详情异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.rule.query", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RuleQueryResponse queryRuleList(QueryListRequest req) {
		LOGGER.debug("===获取预警规则列表接口(mapps.servicemanager.rule.query)入口===");
		RuleQueryResponse response = new RuleQueryResponse();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if (req.getIsenabled() != null) {
				map.put("isenabled", req.getIsenabled());
			}
			if (req.getKeyword() != null) {
				map.put("keyword", "%" + req.getKeyword() + "%");
			}
			if (StringUtil.isNotEmpty(req.getSort())) {
				String sort = "";
				if (req.getSort().contains("resId")) {
					sort = req.getSort().replaceAll("resId", "res_id");
				} else {
					sort = req.getSort();
				}
				map.put("sort", sort);
			}
			PageHelper.startPage(req.getOffset(), req.getLimit());
			List<McAlertRule> list = alertRuleMapper.getQueryList(map);
			for (McAlertRule mar : list) {
				McMeasurement mmt = measurementMapper.selectByPrimaryKey(mar.getMeasurement());
				McValueField mvf = valueFieldMapper.selectByPrimaryKey(mar.getValueField());
				if(mmt != null){
					mar.setMeasurement(mmt.getMeasurement());
				}
				if(mvf != null){
					mar.setValueField(mvf.getField());
				}
			}
			PageInfo<McAlertRule> page = new PageInfo<McAlertRule>(list);
			response.setTotal(page.getTotal());
			response.setRuleList(list);
			LOGGER.debug("获取预警规则列表成功");
		} catch (Exception e) {
			LOGGER.error("获取预警规则列表异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.rule.disable", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse stopRule(IdRequest req) {
		LOGGER.debug("===禁用预警规则接口(mapps.servicemanager.rule.disable)===");
		BaseResponse response = new BaseResponse();
		try {
			McAlertRule mar = new McAlertRule();
			mar.setId(req.getId());
			mar.setEnabled("0");
			alertRuleMapper.updateByPrimaryKeySelective(mar);
			KapacitorUtil kapacitor = new KapacitorUtil(kapacitorServiceUrl);
			McAlertCondition mac = new McAlertCondition();
			mac.setRuleId(req.getId());
			List<McAlertCondition> conditionList = alertConditionMapper.select(mac);
			for (McAlertCondition macForenable : conditionList) {
				kapacitor.disableTask(macForenable.getId());
			}
			LOGGER.debug("禁用预警规则成功");
		} catch (Exception e) {
			LOGGER.error("禁用预警规则异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.rule.enable", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse startRule(IdRequest req) {
		LOGGER.debug("===启用预警规则接口(mapps.servicemanager.rule.enable)===");
		BaseResponse response = new BaseResponse();
		try {
			McAlertRule mar = new McAlertRule();
			mar.setId(req.getId());
			mar.setEnabled("1");
			alertRuleMapper.updateByPrimaryKeySelective(mar);
			KapacitorUtil kapacitor = new KapacitorUtil(kapacitorServiceUrl);
			McAlertCondition mac = new McAlertCondition();
			mac.setRuleId(req.getId());
			List<McAlertCondition> conditionList = alertConditionMapper.select(mac);
			for (McAlertCondition macForenable : conditionList) {
				kapacitor.enableTask(macForenable.getId());
			}
			LOGGER.debug("启用预警规则成功");
		} catch (Exception e) {
			LOGGER.error("启用预警规则异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.rule.save", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse saveRule(RuleSaveRequest req) {
		LOGGER.debug("===保存预警规则接口(mapps.servicemanager.rule.save),请求参数===" + req.getRuleSaveJson());
		BaseResponse response = new BaseResponse();
		try {
			RuleSaveRequest ruleInfo = (RuleSaveRequest) JsonUtil.jsonToObject(req.getRuleSaveJson(),
					RuleSaveRequest.class);
			String dbName = mss.getCurrentSystem().getDb();
			KapacitorUtil kapacitor = new KapacitorUtil(kapacitorServiceUrl);

			McAlertRule alertRule = ruleInfo.getAlertRule();
			alertRule.setSystemId(mss.getCurrentSystem().getId());
			alertRule.setName(JsEscape.unescape(alertRule.getName()));
			alertRule.setMessage(JsEscape.unescape(alertRule.getMessage()).replace("%2B", "+"));

			// 拼接queryQl ---start
			McValueField mvfql = valueFieldMapper.selectByPrimaryKey(alertRule.getValueField());
			McMeasurement mmql = measurementMapper.selectByPrimaryKey(alertRule.getMeasurement());
			String queryQl = "SELECT " + alertRule.getFunc() + "(\"" + mvfql.getField() + "\") FROM \"" + dbName
					+ "\".\""+alertRule.getRp()+"\".\"" + mmql.getMeasurement() + "\"";
			if(!"".equals(alertRule.getWhEre())){
				queryQl += " WHERE " + alertRule.getWhEre();
			}
			alertRule.setQueryQl(queryQl);
			// 拼接queryQl ---end
			
			//处理实时field统计
			if("".equals(alertRule.getFunc())){
				alertRule.setFunc(mvfql.getField());
			}

			List<McAlertMethod> alertMethodList = ruleInfo.getAlertMethodList();
			List<McAlertCondition> alertConditionList = ruleInfo.getAlertConditionList();

			String ruleId = IDGen.uuid().replaceAll("-", "");
			if ("".equals(alertRule.getId())) {
				McAlertRule exsitrule = new McAlertRule();
				exsitrule.setMeasurement(alertRule.getMeasurement());
				exsitrule.setValueField(alertRule.getValueField());
				//List<McAlertRule> exsitrulerlt = alertRuleMapper.select(exsitrule);
				McAlertRule exsitNamerule = new McAlertRule();
				exsitNamerule.setName(alertRule.getName());
				List<McAlertRule> exsitNamerulerlt = alertRuleMapper.select(exsitNamerule);
				/*if (exsitrulerlt.size() > 0) {
					ErrorCode.fail(response, ErrorCode.CODE_300053);
					return response;
				} else*/ if (exsitNamerulerlt.size() > 0) {
					ErrorCode.fail(response, ErrorCode.CODE_300057);
					return response;
				} else {
					alertRule.setEnabled("1");
					alertRule.setId(ruleId);
					alertRuleMapper.insertSelective(alertRule);
				}
			} else {
				alertRule.setEnabled(null);
				ruleId = alertRule.getId();
				alertRuleMapper.updateByPrimaryKeySelective(alertRule);

				// 删除condition和method
				McAlertMethod mam = new McAlertMethod();
				mam.setRuleId(alertRule.getId());
				List<McAlertMethod> mamlt = alertMethodMapper.select(mam);
				for (McAlertMethod am : mamlt) {
					alertMethodMapper.delete(am);
				}

				McAlertCondition mac = new McAlertCondition();
				mac.setRuleId(alertRule.getId());
				List<McAlertCondition> maclt = alertConditionMapper.select(mac);
				for (McAlertCondition ac : maclt) {
					alertConditionMapper.delete(ac);
					kapacitor.deleteTask(ac.getId());
				}
			}

			for (McAlertCondition mac : alertConditionList) {
				String macId = IDGen.uuid().replaceAll("-", "");
				mac.setId(macId);
				mac.setRuleId(ruleId);
				alertConditionMapper.insertSelective(mac);
			}

			for (McAlertMethod mam : alertMethodList) {
				mam.setId(IDGen.uuid().replaceAll("-", ""));
				mam.setRuleId(ruleId);
				alertMethodMapper.insertSelective(mam);
			}

			// 创建tickScript 脚本
			McAlertRule ruleForGetId = alertRuleMapper.selectByPrimaryKey(ruleId);
			Map<String, String> scriptList = TickScriptTemplate.template(alertRule, alertConditionList, alertMethodList,
					smsPushKey, kapacitorOsName);
			for (Map.Entry<String, String> entry : scriptList.entrySet()) {
				String taskId = entry.getKey();
				String script = entry.getValue();
				LOGGER.debug("创建tickScript脚本:\n" + entry.getValue());
				// 创建kapacitor Task
				kapacitor.createTask(taskId, "batch", " [{\"db\": \"" + dbName + "\", \"rp\" : \"autogen\"}]", script,
						("1".equals(ruleForGetId.getEnabled()) ? "enabled" : ""));
			}
			LOGGER.debug("保存预警规则成功");
		} catch (Exception e) {
			LOGGER.error("保存预警规则异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.rule.detail", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RuleDetailResponse getRuleDetail(IdRequest req) {
		LOGGER.debug("===获取预警规则详情接口(mapps.servicemanager.rule.detail),请求参数===" + LogUtil.getObjectInfo(req));
		RuleDetailResponse response = new RuleDetailResponse();
		try {
			McAlertRule alertRule = alertRuleMapper.selectByPrimaryKey(req.getId());
			response.setAlertRule(alertRule);

			McAlertMethod mam = new McAlertMethod();
			mam.setRuleId(req.getId());
			List<McAlertMethod> alertMethodList = alertMethodMapper.select(mam);
			response.setAlertMethodList(alertMethodList);

			McAlertCondition mac = new McAlertCondition();
			mac.setRuleId(req.getId());
			List<McAlertCondition> alertConditionList = alertConditionMapper.select(mac);
			response.setAlertConditionList(alertConditionList);

			LOGGER.debug("获取预警规则详情成功");
		} catch (Exception e) {
			LOGGER.error("获取预警规则详情异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.filter.get", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public KapcitorFilterResponse getTagfilter(IdRequest req) {
		LOGGER.debug("===获取过滤条件接口(mapps.servicemanager.filter.get),请求参数===" + LogUtil.getObjectInfo(req));
		KapcitorFilterResponse response = new KapcitorFilterResponse();
		InfluxdbUtil influxdb = null;
		try {
			McSystem ms = mss.getCurrentSystem();
			influxdb = new InfluxdbUtil(influxdbServiceUrl, ms.getDb(), adminUser, adminPass);
			List<Result> queryResult = influxdb.query("show tag keys from \"" + req.getId() + "\"");
			List<RsKeyValue> filterList = new ArrayList<RsKeyValue>();
			if(queryResult.get(0).getSeries() != null){
				List<McTag> tagList = tagMapper.selectAll();
				List<List<Object>> tagKeyList = queryResult.get(0).getSeries().get(0).getValues();
				for (List<Object> tagKeySet : tagKeyList) {
					String tagKey = (String) tagKeySet.get(0);
					String tagValue = "";
					List<Result> tagValueResult = influxdb
							.query("show tag values from \"" + req.getId() + "\" with key = \"" + tagKey + "\"");
					List<List<Object>> tagValueList = tagValueResult.get(0).getSeries().get(0).getValues();
					for (List<Object> tagValueSet : tagValueList) {
						String oneTagValue = (String) tagValueSet.get(1);
						if ("".equals(tagValue)) {
							tagValue += oneTagValue;
						} else {
							tagValue += "," + oneTagValue;
						}
					}
					RsKeyValue kv = new RsKeyValue();
					//添加中文说明
					String name = "";
					for(McTag tag : tagList){
						if(tagKey.equals(tag.getTag())){
							name = tag.getName();
						}
					}
					if(!"".equals(name)){
						name = "(" + name + ")";
					}
					kv.setKey(tagKey+name);
					kv.setValue(tagValue);
					filterList.add(kv);
				}
				response.setFilterList(filterList);
			}
			LOGGER.debug("获取过滤条件成功");
		} catch (Exception e) {
			LOGGER.error("获取过滤条件异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		} finally {
			if (influxdb != null) {
				influxdb.close();
			}
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.influx.validatesql", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse validateInfluxSql(InfluxSqlRequest req) {
		LOGGER.debug("===influxsql测试接口(mapps.servicemanager.influx.validatesql),请求参数===" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		InfluxdbUtil influxdb = null;
		try {
			// influxdb检测sql
			McSystem ms = mss.getCurrentSystem();
			influxdb = new InfluxdbUtil(influxdbServiceUrl, ms.getDb(), adminUser, adminPass);
			influxdb.query(req.getSql());

			LOGGER.debug("influxsql测试成功");
		} catch (Exception e) {
			LOGGER.error("influxsql测试异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
			String errMsg = e.getMessage();
			if(errMsg.indexOf("error parsing query") > -1){
				response.setError_message("表达式不符合规则");
			}
			return response;
		} finally {
			if (influxdb != null) {
				influxdb.close();
			}
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.echartseries.get", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public InfluxdbSeriesResponse getEchartsSeries(InfluxSqlRequest req) {
		LOGGER.debug("===获取echart数据接口(mapps.servicemanager.echartseries.get),请求参数===" + LogUtil.getObjectInfo(req));
		InfluxdbSeriesResponse response = new InfluxdbSeriesResponse();
		InfluxdbUtil influxdb = null;
		try {
			// influxdb检测sql
			McSystem ms = mss.getCurrentSystem();
			influxdb = new InfluxdbUtil(influxdbServiceUrl, ms.getDb(), adminUser, adminPass);
			String sqlListStr = req.getSql();
			JSONArray sqlList = JSONArray.parseArray(sqlListStr);
			List<Series> series = new ArrayList<Series>();
			for(int i=0;i<sqlList.size();i++){
				String sql = sqlList.getString(i);
				List<Result> queryResult = influxdb.query(sql);
				if(queryResult.size() > 0 && queryResult.get(0).getSeries() != null){
					if(queryResult.get(0).getSeries().size() > 0){
						for(Series ss : queryResult.get(0).getSeries()){
							List<String> columns = ss.getColumns();
							String measurement = ss.getName();
							for(int l=1;l<columns.size();l++){
								String oneColumn = columns.get(l);
								String funcStr = "";
								if(oneColumn.indexOf("_") > -1){
									String func = oneColumn.split("_")[0];
									switch (func) {
									case "mean":
										funcStr = "平均值";
										break;
									case "min":
										funcStr = "最小值";
										break;
									case "max":
										funcStr = "最大值";
										break;
									case "count":
										funcStr = "计数";
										break;
									case "sum":
										funcStr = "求和";
										break;
									case "stddev":
										funcStr = "标准差";
										break;
									}
									oneColumn = oneColumn.split("_")[1];
								}
								McMeasurement mm = new McMeasurement();
								mm.setMeasurement(measurement);
								McMeasurement mmlt = measurementMapper.selectOne(mm);
								McValueField mvf = new McValueField();
								mvf.setMeasurementId(mmlt.getId());
								mvf.setField(oneColumn);
								McValueField mvflt = valueFieldMapper.selectOne(mvf);
								if(mvflt != null){
									columns.set(l, mvflt.getName()+funcStr);
								}else{
									columns.set(l, oneColumn+funcStr);
								}
							}
							ss.setColumns(columns);
							series.add(ss);
						}
					}
				}
			}
			response.setSeries(series);
			LOGGER.debug("获取echart数据成功");
		} catch (Exception e) {
			LOGGER.error("获取echart数据异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
			return response;
		} finally {
			if (influxdb != null) {
				influxdb.close();
			}
		}
		return response;
	}
	
	
	@ServiceMethod(method = "mapps.servicemanager.alertlog.query", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public AlertlogsQueryResponse queryAlertlogList(QueryListRequest req) {
		LOGGER.debug("===获取报警日志列表接口(mapps.servicemanager.alertlog.query)入口===");
		AlertlogsQueryResponse response = new AlertlogsQueryResponse();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			McSystem ms = mss.getCurrentSystem();
			map.put("system_id", ms.getId());
			if (req.getKeyword() != null) {
				map.put("keyword", "%" + req.getKeyword() + "%");
			}
			if (StringUtil.isNotEmpty(req.getSort())) {
				String sort = "";
				if (req.getSort().contains("alertLevel")) {
					sort = req.getSort().replaceAll("alertLevel", "alert_level");
				} else if (req.getSort().contains("alertTime")) {
					sort = req.getSort().replaceAll("alertTime", "alert_time");
				} else {
					sort = req.getSort();
				}
				map.put("sort", sort);
			}
			PageHelper.startPage(req.getOffset(), req.getLimit());
			List<ClientAlertlogInfo> list = alertLogsMapper.getAlertlogsList(map);
			PageInfo<ClientAlertlogInfo> page = new PageInfo<ClientAlertlogInfo>(list);
			response.setTotal(page.getTotal());
			if (list == null) {
				list = new ArrayList<ClientAlertlogInfo>();
			}
			response.setAlertlogsList(list);;
			LOGGER.debug("获取报警日志列表成功");
		} catch (Exception e) {
			LOGGER.error("获取报警日志列表异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	public void saveAlertLog(String ruleId,String content) {
		try {
			JSONObject alertJson =  JSONObject.parseObject(content);
			McAlertLogs alertLogs = new McAlertLogs();
			alertLogs.setId(IDGen.uuid().replaceAll("-", ""));
			alertLogs.setRuleId(ruleId);
			alertLogs.setAlertTime(new Date());
			alertLogs.setAlertLevel((String)alertJson.get("level"));
			alertLogs.setMessage((String)alertJson.get("message"));
			alertLogs.setAlertData(content);
			alertLogsMapper.insertSelective(alertLogs);
			LOGGER.debug("===保存告警日志成功===");
		} catch (Exception e) {
			LOGGER.error("保存告警日志异常：{}", e);
		}
	}

}
