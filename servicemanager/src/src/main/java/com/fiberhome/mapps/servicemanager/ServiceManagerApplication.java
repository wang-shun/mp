package com.fiberhome.mapps.servicemanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.intergration.mybatis.MyMapperScan;
import com.fiberhome.mapps.intergration.security.sso.SsoFilter;
import com.fiberhome.mapps.intergration.session.RopSessionFilter;
import com.fiberhome.mapps.redismq.pubsub.PubSubConfig;
import com.fiberhome.mapps.servicemanager.listener.EurekaInstanceRegisterListener;
import com.fiberhome.mapps.servicemanager.service.DBPasswordEncrypt;
import com.rop.RopServlet;

@Configuration
@ComponentScan()
@EnableAutoConfiguration()
@EnableEurekaServer
@EnableHystrixDashboard
@RestController
@MyMapperScan(basePackages = "com.fiberhome.mapps.servicemanager.dao", markerInterface = MyMapper.class)
@ImportResource("classpath:applicationContext-openapi.xml")

@Import(PubSubConfig.class)
public class ServiceManagerApplication extends WebMvcConfigurerAdapter {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ServiceManagerApplication.class)
			.web(true)
			.listeners(new EurekaInstanceRegisterListener())
			.run(args);
	}

	@Autowired
	DataSource dataSource;

	@Value("${flywaydb.locations}")
	String sqlLocation;

	@Bean(name = "flyway", initMethod = "migrate")
	public Flyway flywayNotADestroyer() {
		Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource);
		flyway.setBaselineOnMigrate(true);
		flyway.setBaselineVersionAsString("0.0.1");
		flyway.setLocations(sqlLocation);

		// 需要修改Schema管理的表名，以区别不同应用/服务
		flyway.setTable("_sm_schema_version");

		return flyway;
	}

	@RequestMapping("/version")
	public String version() throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("build.json");

		try {
			String json = IOUtils.toString(is);
			return json;
		} finally {
			is.close();
		}
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		return new ServletRegistrationBean(new RopServlet(), "/api/*");// ServletName默认值为首字母小写，即myServlet
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.ignoreAcceptHeader(true).defaultContentType(MediaType.APPLICATION_JSON);
	}
	
	@Autowired
    SsoFilter ssoFilter;
    
    @Autowired
    RopSessionFilter sessionFilter;
    
    @Autowired
    UserAgentFakeFilter fakeFilter;
    
    @Bean
    public FilterRegistrationBean registerSsoFilter() {
    	FilterRegistrationBean registrationBean=new FilterRegistrationBean();
		registrationBean.setFilter(ssoFilter);
		List<String> urlPatterns=new ArrayList<String>();
		urlPatterns.add("/api/*"); //拦截路径，可以添加多个
		urlPatterns.add("/index.html"); //拦截路径，可以添加多个
		urlPatterns.add("/websso"); //拦截路径，可以添加多个
		registrationBean.setUrlPatterns(urlPatterns);
		registrationBean.setOrder(10);
		return registrationBean;
    }
    
    @Bean
    public FilterRegistrationBean registerSessionFilter() {
    	FilterRegistrationBean registrationBean=new FilterRegistrationBean();
		registrationBean.setFilter(sessionFilter);
		List<String> urlPatterns=new ArrayList<String>();
		urlPatterns.add("/api/*"); //拦截路径，可以添加多个
		urlPatterns.add("/index.html"); //拦截路径，可以添加多个
		urlPatterns.add("/websso"); //拦截路径，可以添加多个
		registrationBean.setUrlPatterns(urlPatterns);
		registrationBean.setOrder(20);
		return registrationBean;
    }
    
    @Bean
    public FilterRegistrationBean registerFakeFilter() {
    	FilterRegistrationBean registrationBean=new FilterRegistrationBean();
		registrationBean.setFilter(fakeFilter);
		List<String> urlPatterns=new ArrayList<String>();
		urlPatterns.add("/index.html"); //拦截路径，可以添加多个
		registrationBean.setUrlPatterns(urlPatterns);
		registrationBean.setOrder(5);
		return registrationBean;
    }
    
    @Bean
    public DBPasswordEncrypt dbPasswordEncrypt(){
    	return new DBPasswordEncrypt();
    }
}
