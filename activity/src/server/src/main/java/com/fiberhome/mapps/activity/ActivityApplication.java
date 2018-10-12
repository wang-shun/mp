package com.fiberhome.mapps.activity;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.intergration.mybatis.MyMapperScan;
import com.fiberhome.mapps.intergration.security.sso.SsoFilter;
import com.fiberhome.mapps.intergration.session.RopSessionFilter;
import com.fiberhome.mapps.intergration.session.SessionCacheFilter;
import com.fiberhome.mapps.mssdk.ConfigClientConfiguration;
import com.rop.RopServlet;


@Configuration
@ComponentScan
@EnableAutoConfiguration()
@EnableScheduling
@EnableAsync
@EnableEurekaClient
@SpringBootApplication
@MyMapperScan(basePackages = "com.fiberhome.mapps.activity.dao", markerInterface = MyMapper.class)
@ImportResource("classpath:applicationContext-openapi.xml")
public class ActivityApplication {
    public static void main(String[] args) {
    	ConfigClientConfiguration ccc = new ConfigClientConfiguration("mapps-activity");
        new SpringApplicationBuilder(ActivityApplication.class).initializers(ccc).web(true).run(args);
        //new SpringApplicationBuilder(ActivityApplication.class).web(true).run(args);
    }
    
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new RopServlet(), "/api/*");// ServletName默认值为首字母小写，即myServlet
    }
    
    @Autowired
    SessionCacheFilter sessionCacheFilter;
    
    @Autowired
    SsoFilter ssoFilter;
    
    @Autowired
    RopSessionFilter sessionFilter;
    
    @Autowired
    UserAgentFakeFilter fakeFilter;
    
    @Bean
    public FilterRegistrationBean registerSessionCacheFilter() {
    	FilterRegistrationBean registrationBean=new FilterRegistrationBean();
		registrationBean.setFilter(sessionCacheFilter);
		List<String> urlPatterns=new ArrayList<String>();
		urlPatterns.add("/api/*"); //拦截路径，可以添加多个
		urlPatterns.add("/index.html"); //拦截路径，可以添加多个
		urlPatterns.add("/websso"); //拦截路径，可以添加多个
		registrationBean.setUrlPatterns(urlPatterns);
		registrationBean.setOrder(5);
		return registrationBean;
    }
    
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
    
    @Autowired
    DataSource dataSource;
    
    @Value("${flywaydb.locations}")
    String sqlLocation;
    
    @Bean(name = "flyway")
    public Flyway flywayNotADestroyer()
    {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersionAsString("0.0.1");
        flyway.setLocations(sqlLocation);

        // 需要修改Schema管理的表名，以区别不同应用/服务
        flyway.setTable("_at_schema_version");
        
        try {
        	flyway.migrate();
    	} catch (Exception ex) {
        	ex.printStackTrace();
    	}

        return flyway;
    }   
}
