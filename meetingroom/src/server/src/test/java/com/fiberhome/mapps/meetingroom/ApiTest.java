package com.fiberhome.mapps.meetingroom;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fiberhome.mapps.roptest.ApiTestSuite;

// 通过注解，使用Junit4进行单元测试
@RunWith(SpringJUnit4ClassRunner.class)
// 启动的服务程序    支持web集成测试，以随机端口启动该服务
@SpringBootTest(classes = MockApplication.class,webEnvironment=WebEnvironment.RANDOM_PORT)
public class ApiTest
{
    // 注入随机启动的服务端口
    @Value("${local.server.port}")
    private int    port;

    // api的路径，默认为/api, 在application.yml中进行配置
    @Value("${api.endpoint}")
    private String endpoint;

    // 测试方案
    ApiTestSuite   ats;

    @Before
    public void setUp() throws IOException
    {
        String serviceUrl = "http://localhost:" + port + endpoint;
        // 装载测试用例配置文件，并初始化测试方案
        // ats = new ApiTestSuite("classpath*:testcase/*.yml" , serviceUrl);
    }

    @Test
    public void testApi()
    {
        // 运行单元测试
        // ats.testRun();
    }
}
