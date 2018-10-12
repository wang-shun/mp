package com.fiberhome.mapps.roptest;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.yaml.snakeyaml.Yaml;

import junit.framework.TestResult;
import junit.framework.TestSuite;

public class ApiTestSuite {
	private final static Logger LOG = LoggerFactory.getLogger(ApiTestSuite.class);
	
	private String casePath;

	private String serviceUrl;

	public ApiTestSuite(String serviceUrl) {
		this("classpath*:testcase/*.yml", serviceUrl);
	}

	public ApiTestSuite(String casePath, String serviceUrl) {
		this.casePath = casePath;
		this.serviceUrl = serviceUrl;
	}

	public String getCasePath() {
		return casePath;
	}

	public void setCasePath(String casePath) {
		this.casePath = casePath;
	}

	
	public void testRun()  {
		try {
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources("classpath*:testcase/*.yml");
			List<ApiTestCase> testcases = new ArrayList<ApiTestCase>();
			
			for (Resource res : resources) {
				LOG.info("loading case file：{}", res.getFilename());
				Yaml yaml = new Yaml();

				Map tcases = (Map) yaml.load(res.getInputStream());

				LOG.info("Test Case：{}", tcases);
				
				testcases.addAll(ApiTestCase.build(tcases, serviceUrl));

				for (ApiTestCase testcase : testcases) {
					LOG.info("Case Name: {}", testcase.getName());
					LOG.info("AssertRules: {}", testcase.getAssertRules());
				}
			}
			
			TestSuite ts = new TestSuite();

			for (ApiTestCase atc : testcases) {
				ts.addTest(atc);
			}
			TestResult result = new TestResult();
			ts.run(result);

			if (result.wasSuccessful()) {
				LOG.info("用例测试通过!");
			} else {
				assertTrue(false);
				LOG.warn("用例测试不通过!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
