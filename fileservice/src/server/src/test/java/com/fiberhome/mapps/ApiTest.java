package com.fiberhome.mapps;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fiberhome.mapps.fileservice.FileServiceApplication;
import com.fiberhome.mapps.roptest.ApiTestSuite;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=FileServiceApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class ApiTest {
	
	@Value("${local.server.port}")
    private int port = 9081;
	
	@Value("${api.endpoint}")
	private String endpoint = "/api";
	
	ApiTestSuite ats;

	@Before
	public void setUp() throws IOException {
		String serviceUrl = "http://localhost:"+ port + endpoint;
		ats = new ApiTestSuite(serviceUrl);
	}

	@Test
	public void testApi() {
		
		try {
			ats.testRun();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
