package com.fiberhome.mapps.servicemanager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.influxdb.dto.QueryResult.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.servicemanager.dao.McDashboardMapper;
import com.fiberhome.mapps.servicemanager.dao.McDashboardPanelMapper;
import com.fiberhome.mapps.servicemanager.dao.McDashboardPanelSeriesMapper;
import com.fiberhome.mapps.servicemanager.entity.DisplayEchartsInfo;
import com.fiberhome.mapps.servicemanager.entity.McDashboard;
import com.fiberhome.mapps.servicemanager.entity.McDashboardPanel;
import com.fiberhome.mapps.servicemanager.entity.McDashboardPanelSeries;
import com.fiberhome.mapps.servicemanager.entity.McSystem;
import com.fiberhome.mapps.servicemanager.request.DashboardPanelSaveRequest;
import com.fiberhome.mapps.servicemanager.request.DashboardSaveRequest;
import com.fiberhome.mapps.servicemanager.request.DisplayEchartsRequest;
import com.fiberhome.mapps.servicemanager.request.IdRequest;
import com.fiberhome.mapps.servicemanager.request.InfluxSqlRequest;
import com.fiberhome.mapps.servicemanager.request.QueryListRequest;
import com.fiberhome.mapps.servicemanager.request.RenameRequest;
import com.fiberhome.mapps.servicemanager.response.DashboardDetailResponse;
import com.fiberhome.mapps.servicemanager.response.DashboardListResponse;
import com.fiberhome.mapps.servicemanager.response.DisplayEchartsResponse;
import com.fiberhome.mapps.servicemanager.response.IdResponse;
import com.fiberhome.mapps.servicemanager.response.InfluxdbSeriesResponse;
import com.fiberhome.mapps.servicemanager.response.PanelDetailResponse;
import com.fiberhome.mapps.servicemanager.utils.ErrorCode;
import com.fiberhome.mapps.servicemanager.utils.IDGen;
import com.fiberhome.mapps.servicemanager.utils.JsEscape;
import com.fiberhome.mapps.servicemanager.utils.JsonUtil;
import com.fiberhome.mapps.servicemanager.utils.LogUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean
public class DashboardManagerService {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	McSystemService mss;
	
	@Autowired
	McDashboardMapper dashboardMapper;
	
	@Autowired
	McDashboardPanelMapper dashboardPanelMapper;
	
	@Autowired
	McDashboardPanelSeriesMapper dashboardPanelSeriesMapper;
	
	@Autowired
	MonitorManagerService mms;
	
	@Value("${flywaydb.locations}")
	String flywaydbLocations;

	@ServiceMethod(method = "mapps.servicemanager.dashboard.list", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public DashboardListResponse getDashboardList(QueryListRequest req) {
		LOGGER.debug("===获取dashboard列表接口(mapps.servicemanager.dashboard.list)===");
		DashboardListResponse response = new DashboardListResponse();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if (req.getKeyword() != null) {
				map.put("keyword", "%" + req.getKeyword() + "%");
			}
			if (StringUtil.isNotEmpty(req.getSort()))
	        {
	            map.put("sort", req.getSort());
	        }
			PageHelper.startPage(req.getOffset(), req.getLimit());
			List<McDashboard> dashboardList = dashboardMapper.getDashboardList(map);
			PageInfo<McDashboard> page = new PageInfo<McDashboard>(dashboardList);
			response.setTotal(page.getTotal());
			if (dashboardList == null) {
				dashboardList = new ArrayList<McDashboard>();
			}
			
			
			response.setDashboardList(dashboardList);
			LOGGER.debug("获取dashboard列表成功");
		} catch (Exception e) {
			LOGGER.error("获取dashboard列表异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.dashboard.save", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public IdResponse saveDashboard(DashboardSaveRequest req) {
		LOGGER.debug("===保存dashboard接口(mapps.servicemanager.dashboard.save),请求参数===" + req.getDashboardSaveJson());
		IdResponse response = new IdResponse();
		try {
			DashboardSaveRequest dashboardInfo = (DashboardSaveRequest) JsonUtil
					.jsonToObject(req.getDashboardSaveJson(), DashboardSaveRequest.class);
			McDashboard mdb = dashboardInfo.getDashboard();
			String id = IDGen.uuid().replaceAll("-", "");
			mdb.setName(JsEscape.unescape(mdb.getName()));
			if ("".equals(mdb.getId())) {
				mdb.setIsDefault("0");
				mdb.setId(id);
				dashboardMapper.insertSelective(mdb);
			} else {
				id = mdb.getId();
				McDashboard mdbForUPD = dashboardMapper.selectByPrimaryKey(id);
				mdb.setIsDefault(mdbForUPD.getIsDefault());
				dashboardMapper.updateByPrimaryKeySelective(mdb);
			}
			response.setId(id);
			LOGGER.debug("保存dashboard成功");
		} catch (Exception e) {
			LOGGER.error("保存dashboard异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.dashboard.detail", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public DashboardDetailResponse getDashboardDetail(IdRequest req) {
		LOGGER.debug("===获取dashboard详情接口(mapps.servicemanager.dashboard.detail),请求参数===" + LogUtil.getObjectInfo(req));
		DashboardDetailResponse response = new DashboardDetailResponse();
		try {
			McDashboard mdb = dashboardMapper.selectByPrimaryKey(req.getId());
			response.setDashboard(mdb);
			LOGGER.debug("获取dashboard详情成功");
		} catch (Exception e) {
			LOGGER.error("获取dashboard详情异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.dashboard.delete", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse deleteDashboard(IdRequest req) {
		LOGGER.debug("===删除dashboard接口(mapps.servicemanager.dashboard.delete),请求参数===" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			dashboardMapper.deleteByPrimaryKey(req.getId());
			
			McDashboardPanel mdpForDel = new McDashboardPanel();
			mdpForDel.setDashboardId(req.getId());
			dashboardPanelMapper.delete(mdpForDel);
			
			McDashboardPanelSeries mdpsForDel = new McDashboardPanelSeries();
			mdpsForDel.setDashboardId(req.getId());
			dashboardPanelSeriesMapper.delete(mdpsForDel);
			LOGGER.debug("删除dashboard成功");
		} catch (Exception e) {
			LOGGER.error("删除dashboard异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.dashboard.setdefault", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse setDefaultDashboard(IdRequest req) {
		LOGGER.debug("===设置默认dashboard接口(mapps.servicemanager.dashboard.setdefault),请求参数===" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			McDashboard mdForCancelDefault = new McDashboard();
			mdForCancelDefault.setIsDefault("1");
			List<McDashboard> mdForCancelDefaultList = dashboardMapper.select(mdForCancelDefault);
			for(McDashboard mdfcd : mdForCancelDefaultList){
				mdfcd.setIsDefault("0");
				dashboardMapper.updateByPrimaryKeySelective(mdfcd);
			}
			McDashboard md = new McDashboard();
			md.setId(req.getId());
			md.setIsDefault("1");
			dashboardMapper.updateByPrimaryKeySelective(md);
			LOGGER.debug("设置默认dashboard成功");
		} catch (Exception e) {
			LOGGER.error("设置默认dashboard异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.dashboard.displayecharts", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public DisplayEchartsResponse displayDashboardEcharts(DisplayEchartsRequest req) {
		LOGGER.debug("===展示dashboard图表接口(mapps.servicemanager.dashboard.displayecharts),请求参数===" + LogUtil.getObjectInfo(req));
		DisplayEchartsResponse response = new DisplayEchartsResponse();
		try {
			//用于建立中文图例
			int customNum = 1;
			JSONObject customMap = new JSONObject();
			
			List<DisplayEchartsInfo> displayEchartsList = new ArrayList<DisplayEchartsInfo>();
			McSystem ms = mss.getCurrentSystem();
			JSONArray idList = JSONArray.parseArray(req.getIdList());
			for(int m=0;m<idList.size();m++){
				String panelId = idList.getString(m);
				McDashboardPanel dashboardPanel = dashboardPanelMapper.selectByPrimaryKey(panelId);
				McDashboardPanelSeries mdpsForQuery = new McDashboardPanelSeries();
				mdpsForQuery.setPanelId(panelId);
				List<McDashboardPanelSeries> dashboardPanelSeries = null;
				if(flywaydbLocations.contains("mysql")){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", panelId);
					dashboardPanelSeries = dashboardPanelSeriesMapper.selectMysql(map);
				}else{
					dashboardPanelSeries = dashboardPanelSeriesMapper.select(mdpsForQuery);
				}
				JSONArray influxSqlList = new JSONArray();
				for(McDashboardPanelSeries mdps : dashboardPanelSeries){
					JSONObject fieldSetting = JSONObject.parseObject(mdps.getFieldsSetting());
					String influxSql = "select ";
					JSONArray fieldList = fieldSetting.getJSONArray("field");
					for(int i=0;i<fieldList.size();i++){
						String func = fieldList.getJSONObject(i).getString("func");
						String key = fieldList.getJSONObject(i).getString("key");
						String s = (i == 0)?"":",";
						if("".equals(func)){
							influxSql += s + "\"" + key + "\"";
						}else{
							influxSql += s + func + "(\"" + key + "\") as \"" + func + "_" + key + "\"";
						}
					}
					String ss = ("select ".equals(influxSql))?"":",";
					if(!"".equals(fieldSetting.getString("customField"))){
						influxSql += ss + "(" + fieldSetting.getString("customField") + ") as customValue" + customNum;
						customMap.put("customValue" + customNum, fieldSetting.getString("customName"));
						customNum++;
					}
					influxSql += " from \""+ms.getDb()+"\".\""+mdps.getRetentionPolicy()+"\".\""+mdps.getMeasurement()+"\"";
					
					String whereStr = "";
					String groupbyStr = "";
					if(!"".equals(mdps.getWhereSetting())){
						JSONObject whereSetting = JSONObject.parseObject(mdps.getWhereSetting());
						JSONArray whereList = whereSetting.getJSONArray("where");
						if(whereList.size() > 0){
							whereStr += " and (";
							for(int n=0;n<whereList.size();n++){
								String sss = (n == 0)?"":" or ";
								whereStr += sss + "\"" + whereList.getJSONObject(n).getString("key") + "\"='" + whereList.getJSONObject(n).getString("value") + "'";
							}
							whereStr += ")";
						}
						JSONArray groupbyList = whereSetting.getJSONArray("groupby");
						if(groupbyList.size() > 0 ){
							for(int n=0;n<groupbyList.size();n++){
								groupbyStr += ","+groupbyList.getJSONObject(n).getString("key");
							}
						}
					}
					influxSql += " where time > now() - " + req.getTimeRange() + whereStr;
					if(fieldSetting.getBooleanValue("needGroupBy")){
						influxSql += " group by time(" + req.getRefreshTime() + ")" + groupbyStr;
					}else if(!"".equals(groupbyStr)){
						influxSql += " group by " + groupbyStr.substring(1, groupbyStr.length());
					}
					influxSqlList.add(influxSql);
				}
				InfluxSqlRequest isr = new InfluxSqlRequest();
				isr.setSql(influxSqlList.toJSONString());
				InfluxdbSeriesResponse isres = mms.getEchartsSeries(isr);
				List<Series> series = isres.getSeries();
				for(Series oneSeries : series){
					List<String> columns = oneSeries.getColumns();
					for(int l=0;l<columns.size();l++){
						if(columns.get(l).indexOf("customValue") > -1){
							String customName = customMap.getString(columns.get(l));
							if(customName != null){
								columns.set(l, customName);
							}
						}
					}
					oneSeries.setColumns(columns);
				}
				DisplayEchartsInfo dei = new DisplayEchartsInfo();
				dei.setPanel(dashboardPanel);
				dei.setSeries(series);
				displayEchartsList.add(dei);
			}
			response.setDisplayEchartsList(displayEchartsList);
			LOGGER.debug("展示dashboard图表成功");
		} catch (Exception e) {
			LOGGER.error("展示dashboard图表异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.dashboard.panel.save", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public IdResponse saveDashboardPanel(DashboardPanelSaveRequest req) {
		LOGGER.debug("===保存dashboard图表panel接口(mapps.servicemanager.dashboard.panel.save),请求参数===" + LogUtil.getObjectInfo(req));
		IdResponse response = new IdResponse();
		try {
			DashboardPanelSaveRequest dashboardPanelInfo = (DashboardPanelSaveRequest) JsonUtil
					.jsonToObject(req.getDashboardPanelSaveJson(), DashboardPanelSaveRequest.class);
			
			McSystem ms = mss.getCurrentSystem();
			
			String panelId = IDGen.uuid().replaceAll("-", "");
			McDashboardPanel dashboardPanel = dashboardPanelInfo.getDashboardPanel();
			dashboardPanel.setName(JsEscape.unescape(dashboardPanel.getName()));
			if("".equals(dashboardPanel.getId())){
				dashboardPanel.setId(panelId);
				dashboardPanel.setSystemId(ms.getId());
				dashboardPanelMapper.insertSelective(dashboardPanel);
			}else{
				panelId = dashboardPanel.getId();
				dashboardPanelMapper.updateByPrimaryKeySelective(dashboardPanel);
				McDashboardPanelSeries mdpsForDel = new McDashboardPanelSeries();
				mdpsForDel.setPanelId(panelId);
				dashboardPanelSeriesMapper.delete(mdpsForDel);
			}
			
			List<McDashboardPanelSeries> dashboardPanelSeries = dashboardPanelInfo.getDashboardPanelSeries();
			for(McDashboardPanelSeries mdps : dashboardPanelSeries){
				mdps.setId(IDGen.uuid().replaceAll("-", ""));
				mdps.setPanelId(panelId);
				mdps.setFieldsSetting(mdps.getFieldsSetting().replaceAll("%2B", "+"));
				if(flywaydbLocations.contains("mysql")){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", mdps.getId());
					map.put("panelId", mdps.getPanelId());
					map.put("dashboardId", mdps.getDashboardId());
					map.put("retentionPolicy", mdps.getRetentionPolicy());
					map.put("measurement", mdps.getMeasurement());
					map.put("whereSetting", mdps.getWhereSetting());
					map.put("fieldsSetting", mdps.getFieldsSetting());
					map.put("sql", mdps.getSql());
					dashboardPanelSeriesMapper.insertSelectiveMysql(map);
				}else{
					dashboardPanelSeriesMapper.insertSelective(mdps);
				}
			}
			response.setId(panelId);
			LOGGER.debug("保存dashboard图表panel成功");
		} catch (Exception e) {
			LOGGER.error("保存dashboard图表panel异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.dashboard.panel.detail", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public PanelDetailResponse getDashboardPanel(IdRequest req) {
		LOGGER.debug("===获取panel图表详情接口(mapps.servicemanager.dashboard.panel.detail),请求参数===" + LogUtil.getObjectInfo(req));
		PanelDetailResponse response = new PanelDetailResponse();
		try {
			McDashboardPanel dashboardPanel = dashboardPanelMapper.selectByPrimaryKey(req.getId());
			McDashboardPanelSeries mdps = new McDashboardPanelSeries();
			mdps.setPanelId(req.getId());
			List<McDashboardPanelSeries> dashboardPanelSeries = null;
			if(flywaydbLocations.contains("mysql")){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", req.getId());
				dashboardPanelSeries = dashboardPanelSeriesMapper.selectMysql(map);
			}else{
				dashboardPanelSeries = dashboardPanelSeriesMapper.select(mdps);
			}
			response.setDashboardPanel(dashboardPanel);
			response.setDashboardPanelSeries(dashboardPanelSeries);
			LOGGER.debug("获取panel图表详情成功");
		} catch (Exception e) {
			LOGGER.error("获取panel图表详情异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.dashboard.panel.delete", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse deleteDashboardPanel(IdRequest req) {
		LOGGER.debug("===删除panel图表接口(mapps.servicemanager.dashboard.panel.delete),请求参数===" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			dashboardPanelMapper.deleteByPrimaryKey(req.getId());
			McDashboardPanelSeries mdpsForDel = new McDashboardPanelSeries();
			mdpsForDel.setPanelId(req.getId());
			dashboardPanelSeriesMapper.delete(mdpsForDel);
			LOGGER.debug("删除panel图表成功");
		} catch (Exception e) {
			LOGGER.error("删除panel图表异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.dashboard.panel.rename", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse renameDashboardPanel(RenameRequest req) {
		LOGGER.debug("===重命名panel图表接口(mapps.servicemanager.dashboard.panel.rename),请求参数===" + LogUtil.getObjectInfo(req));
		BaseResponse response = new BaseResponse();
		try {
			McDashboardPanel mdp = dashboardPanelMapper.selectByPrimaryKey(req.getId());
			mdp.setName(req.getName());
			dashboardPanelMapper.updateByPrimaryKeySelective(mdp);
			LOGGER.debug("重命名panel图表成功");
		} catch (Exception e) {
			LOGGER.error("重命名panel图表异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
}
