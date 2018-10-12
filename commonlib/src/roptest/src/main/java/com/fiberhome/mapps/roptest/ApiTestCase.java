package com.fiberhome.mapps.roptest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fiberhome.mos.core.openapi.rop.client.RopClient;
import com.fiberhome.mos.core.openapi.rop.client.RopClientException;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import junit.framework.TestResult;

public class ApiTestCase extends TestCase{
	private final static Logger LOG = LoggerFactory.getLogger(ApiTestCase.class);
	
	String name;

	String desc;

	Service service;
	
	Map<String, String> header;

	Map<String, Object> request;

	List<AssertRule> assertRules;
	
	RopClient client;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Map<String, Object> getRequest() {
		return request;
	}

	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

	public List<AssertRule> getAssertRules() {
		return assertRules;
	}

	public void setAssertRules(List<AssertRule> assertRules) {
		this.assertRules = assertRules;
	}
	
	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	@Override
	public void run(TestResult tr) {
		try {
			Map<String, Object> response = client.requestForMap(this.getService().getMethod(), this.getService().getV(), this.getRequest());
			LOG.info("接口调用返回结果：response: {}", response);
			RuleValidator rv = new RuleValidator(response);
			
			LOG.debug("Excute testcase: {}", this.getName());
			for (AssertRule rule : this.getAssertRules()) {
				if (!rv.validate(rule)) {
					tr.addFailure(this, new AssertionFailedError(rule.toString()));
				}
			}
		} catch (RopClientException e) {
			tr.addError(this, e);
			e.printStackTrace();
		}
		tr.endTest(this);
	}
		
	public static List<ApiTestCase> build(Map config, String serviceUrl) {
		List<ApiTestCase> cases = new ArrayList<ApiTestCase>();
		// 处理每个 testcase
		for (Object tcase : config.keySet()) {
			cases.add(build(tcase.toString(), (Map)config.get(tcase), serviceUrl));
		}

		return cases;
	}

	public static ApiTestCase build(String caseName, Map caseConfig, String serviceUrl) {
		ApiTestCase testcase = new ApiTestCase();
		testcase.setName(caseName);
		
		// 获取service
		Service service = new Service();
		testcase.setService(service);
		
		Map svcConfig = (Map)caseConfig.get("service");
		try {
			BeanUtils.populate(service, svcConfig);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO 异常处理
			e.printStackTrace();
		}
		
		RopClient client = new RopClient(serviceUrl, service.getAppKey(), service.getSecret(), service.getFormat());
		if (service.isSession()) {
			client.setSessionId(service.getSessionId());
		}
		testcase.setClient(client);
		
		Map headerConfig = (Map)caseConfig.get("header");
		testcase.setHeader((Map<String, String>)headerConfig);
		
		// 获取request
		testcase.setRequest((Map<String, Object>)caseConfig.get("request"));
		
		// 获取assert rules
		List<AssertRule> rules = new ArrayList<AssertRule>();
		parseRules("", (Map)caseConfig.get("assert"), rules);
		testcase.setAssertRules(rules);
		
		return testcase;
	}
	
	private void setClient(RopClient client) {
		this.client = client;
	}

	public static void parseRules(String prefix, Map config, List<AssertRule> rules) {
		for (Object key : config.keySet()) {
			
			Object value = config.get(key);
			String skey = (String)key;
			
			if (skey.startsWith("_")) { // 操作符
				if (value instanceof Map) {
					throw new IllegalArgumentException("错误的操作符设置：" + prefix + skey);
				} else {
					AssertRule rule = new AssertRule();
					rule.setExpresion(prefix);
					rule.setOperate(skey);
					rule.setResult(value);
					rules.add(rule);
				}
			} else {
				if (value instanceof Map) {
					String newPrefix = prefix.length() > 0 ? prefix + "." + skey : skey;
					parseRules(newPrefix, (Map)value, rules);
				} else {
					throw new IllegalArgumentException("错误的属性校验设置：" + prefix + skey + "，应为Map");
				}
			}
		}
	}

}
