package com.fiberhome.mapps.fileservice;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiberhome.mapps.mssdk.ConfigClientConfiguration;
import com.fiberhome.mapps.fileservice.impl.fdfs.FdfsServiceAccessor;
import com.fiberhome.mapps.fileservice.impl.filesystem.FileSystemServiceAccessor;
import com.github.tobato.fastdfs.FdfsClientConfig;
import com.rop.RopServlet;

@Configuration
@ComponentScan
@EnableAutoConfiguration()
@EnableEurekaClient
@SpringBootApplication
@ImportResource("classpath:applicationContext-openapi.xml")
@Import({FdfsClientConfig.class})
@RestController
public class FileServiceApplication {
	
	@Value("${api.endpoint}")
	String endpoint;
	
	@Value("${eureka.instance.instanceId}")
	String instanceId;
	
	@Value("${fileservice.store}")
	String storeType;
	
    public static void main(String[] args) {
    	ConfigClientConfiguration ccc = new ConfigClientConfiguration("mapps-fileservice");
        new SpringApplicationBuilder(FileServiceApplication.class).initializers(ccc).web(true).run(args);
    }
    
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new RopServlet(), endpoint + "/*");// ServletName默认值为首字母小写，即myServlet
    }
    
    @Bean
    public ServiceAccessor serviceAccessorBean() {
    	//根据 配置参数storeType装载相应操作类
    	if(storeType.equals("file")){
    		return new FileSystemServiceAccessor();
    	}else{
    		return new FdfsServiceAccessor();
    	}
    }
    
    @RequestMapping("/")
    public String home() {
        return "Hello world";
    }
    
    @RequestMapping("/info")
    public String info() {
        return "this is mapps fileservice: " + instanceId;
    }
    
    @RequestMapping("/health")
    public String healthcheck() {
        return "OK";
    }
    
    @Autowired
    DataSource dataSource;
    
    @Value("${flywaydb.locations}")
    String sqlLocation;
    
    @Bean(name = "flyway")    
    public Flyway flywayNotADestroyer() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersionAsString("0.0.1");
        flyway.setLocations(sqlLocation);
        
        // 需要修改Schema管理的表名，以区别不同应用/服务
        flyway.setTable("_fs_schema_version");
        
        try {
        	flyway.migrate();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        return flyway;
    }
    
}
