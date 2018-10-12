package com.fiberhome.mapps.activity;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fiberhome.mapps.intergration.security.sso.UserInfo;
import com.fiberhome.mapps.intergration.security.sso.Validator;
import com.rop.RopServlet;

@Configuration
@ComponentScan
@EnableAutoConfiguration()
@SpringBootApplication
//@ImportResource("classpath:applicationContext-openapi.xml")
public class MockApplication {
	public static void main(String[] args) {
        new SpringApplicationBuilder(MockApplication.class).web(true).run(args);
    }
	
	@Bean
	@Primary
	public Validator mockValidator() {
		Validator validator =  mock(Validator.class);
		
		UserInfo user = new UserInfo();
		user.setAdmin(false);
		user.setEcid("test");
		user.setOrgId("test-org-uuid");
		user.setOrgName("test-org-name");
		user.setUserId("test-user-id");
		user.setUserId("test-user");
		
		when(validator.validate(anyString(), eq(true), eq((String)null), eq((String)null))).thenReturn(user);
		when(validator.getServiceUrl()).thenReturn("http://localhost:8080");
		when(validator.getAppKey()).thenReturn("mr");
		when(validator.getSecret()).thenReturn("FHuma025");
		
		return validator;
	}
	
	@Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new RopServlet(), "/api/*");// ServletName默认值为首字母小写，即myServlet
    }
    
    @Autowired
    DataSource dataSource;
    
    @Bean(name = "flyway", initMethod = "migrate")    
    public Flyway flywayNotADestroyer() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersionAsString("0.0.1");
        
        // 需要修改Schema管理的表名，以区别不同应用/服务
        flyway.setTable("_at_schema_version");
        
        return flyway;
    }
}
