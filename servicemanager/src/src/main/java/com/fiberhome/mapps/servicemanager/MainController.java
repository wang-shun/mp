package com.fiberhome.mapps.servicemanager;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.servicemanager.request.AppIdRequest;
import com.fiberhome.mapps.servicemanager.response.AppConfigResponse;
import com.fiberhome.mapps.servicemanager.response.ResourceGetResponse;
import com.fiberhome.mapps.servicemanager.response.SVCConfigResponse;
import com.fiberhome.mapps.servicemanager.service.ConfigManagerService;
import com.fiberhome.mapps.servicemanager.service.MonitorManagerService;
import com.fiberhome.mapps.servicemanager.utils.ErrorCode;
import com.fiberhome.mos.core.openapi.rop.client.RopClient;

@RestController
@RequestMapping
public class MainController {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Value("${mplus.login.loginPage}")
	String loginPage;

	@Value("${mplus.sso.serviceUrl}")
	String serviceUrl;

	@Value("${mplus.sso.appKey}")
	String appKey;

	@Value("${mplus.sso.secret}")
	String secret;
	
	@Value("${sms.key}")
	String smsPushKey;
	
	@Autowired
	ConfigManagerService cfgms;
	
	@Autowired
	MonitorManagerService mms;

	// https://192.168.160.154:8440/mos/arkContentLogin.action?sessionId=192.168.160.98%2302EFDB2CA42F6EA15D032B2CB8A684A1&orgUuidDefault=8cedb324-fc84-4606-8763-3857618a885b&orgCodeDefault=fiberhome
	@RequestMapping("/websso")
	public ModelAndView webSso(@RequestParam String sessionId,
			@RequestParam(name = "orgUuidDefault", required = false) String orgId,
			@RequestParam(name = "orgCodeDefault", required = false) String ecid,
			@RequestParam(required = false) String forword, @RequestParam(required = false) String forward,
			HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mv = new ModelAndView();

		if (SessionContext.getUserId() == null) {
			//mv.setViewName("redirect:" + loginPage);   // 用户无效切换到登陆页面
			mv.setViewName("/sessionfail.html");
		} else {
			
			// index.html not cached
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragrma", "no-cache");
			response.setDateHeader("Expires", 0);
			// Fix IE Blocking iFrame Cookies
			response.addHeader("P3P", "CP=\"IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT\"");
			System.out.println("==============>" + forword);
			if ((forword == null || "".equals(forword)) && (forward == null || "".equals(forward))) {
				mv.setViewName("/index.html");
			} else if ("monitor".equals(forword) || "monitor".equals(forward)) {
				mv.setViewName("/monitorindex.html");
			} else if ("main".equals(forword) || "main".equals(forward)) {
				mv.setViewName("/main.html");
			} else if ("index".equals(forword) || "index".equals(forward)) {
				mv.setViewName("/index.html");
			} else if ("trace".equals(forword) || "trace".equals(forward)) {
				mv.setViewName("/traceindex.html");
			}
		}

		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "/configSvc/{appId}", produces = "application/json")
	public SVCConfigResponse getConfigSVC(@PathVariable String appId, HttpServletRequest req,
			HttpServletResponse response) {
		LOGGER.debug("===配置获取接口(/configSvc/{appId})入口,请求参数==appId : " + appId);
		SVCConfigResponse res = new SVCConfigResponse();
		try {
			AppIdRequest appIdReq = new AppIdRequest();
			appIdReq.setAppId(appId);
			res.setServices(cfgms.servicesGet(appIdReq));
			ResourceGetResponse rgbai = cfgms.resourceGetByAppId(appIdReq);
			if (rgbai.getCode().equals("1")) {
				res.setResources(rgbai.getResources());
			} else {
				throw new Exception();
			}
			AppConfigResponse acl = cfgms.appConfigLoad(appIdReq);
			if (acl.getCode().equals("1")) {
				res.setConfig(acl.getConfig());
			} else {
				throw new Exception();
			}
			LOGGER.debug("配置获取成功");
		} catch (Exception e) {
			LOGGER.error("配置获取异常：{}", e);
			ErrorCode.fail(res, ErrorCode.CODE_100001);
		}
		return res;
	}

	@RequestMapping(value = "/smspush", method = RequestMethod.POST)
	public void smspush(HttpServletRequest request, HttpServletResponse response) {
		try {
			BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}
			String phoneNumbers = request.getParameter("phoneNumbers").replaceAll("and", ",");
			String reqSmsPushKey = request.getParameter("key");
			LOGGER.debug(
					"===调用mplus短信接口(/smspush)入口,请求参数==reqSmsPushKey : " + reqSmsPushKey + "==phoneNumbers : " + phoneNumbers + "==wholeStr : " + wholeStr);
			if(!smsPushKey.equals(reqSmsPushKey)){
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				LOGGER.debug("===请求接口验证key错误,拒绝访问===");
				return;
			}
			JSONObject alertJson =  JSONObject.parseObject(wholeStr);
			Map<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("phoneNumbers", phoneNumbers);
			reqMap.put("content", alertJson.get("message"));
			RopClient client = new RopClient(serviceUrl, appKey, secret, "json");
			if (!"".equals(phoneNumbers)) {
				Map<String, Object> resMap = client.requestForMap("mobileark.smspush", "1.0", reqMap);
				LOGGER.debug("======响应结果======" + resMap.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/logalert", method = RequestMethod.POST)
	public void logalert(HttpServletRequest request, HttpServletResponse response) {
		try {
			BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}
			String reqSmsPushKey = request.getParameter("key");
			String ruleId = request.getParameter("ruleId");
			LOGGER.debug(
					"===保存告警日志(/logalert)入口,请求参数==reqSmsPushKey : " + reqSmsPushKey + "==ruleId : " + ruleId + "==wholeStr : " + wholeStr);
			if(!smsPushKey.equals(reqSmsPushKey)){
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				LOGGER.debug("===请求接口验证key错误,拒绝访问===");
				return;
			}
			//JSONObject alertJson =  JSONObject.parseObject(wholeStr);
			mms.saveAlertLog(ruleId,wholeStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
