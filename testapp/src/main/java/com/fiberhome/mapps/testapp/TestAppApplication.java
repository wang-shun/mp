package com.fiberhome.mapps.testapp;

import javax.sql.DataSource;

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
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiberhome.mapps.mssdk.ConfigClientConfiguration;
import com.rop.RopServlet;

//@EnableAtlas
@Configuration
@ComponentScan
@EnableAutoConfiguration()
@EnableEurekaClient
@SpringBootApplication
@ImportResource("classpath:applicationContext-openapi.xml")
@RestController
public class TestAppApplication {
	
	@Value("${api.endpoint}")
	String endpoint;
	
	@Value("${eureka.instance.instanceId}")
	String instanceId;
	
    public static void main(String[] args) throws InterruptedException {
    	ConfigClientConfiguration ccc = new ConfigClientConfiguration("mapps-testapp");
        new SpringApplicationBuilder(TestAppApplication.class).initializers(ccc).web(true).run(args);
    }
    
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new RopServlet(), endpoint + "/*");// ServletName默认值为首字母小写，即myServlet
    }
    
    @RequestMapping("/")
    public String home() {
        return "Hello world";
    }
    
    @RequestMapping("/info")
    public String info() {
        return "this is mapps testapp: " + instanceId;
    }
    
    @RequestMapping("/health")
    public String healthcheck() {
        return "OK";
    }
    
    
    @Autowired
    DataSource dataSource;
    
//    @Value("${flywaydb.locations}")
//    String sqlLocation;
    
//    @Bean(name = "flyway", initMethod = "migrate")    
//    public Flyway flywayNotADestroyer() {
//        Flyway flyway = new Flyway();
//        flyway.setDataSource(dataSource);
//        flyway.setBaselineOnMigrate(true);
//        flyway.setBaselineVersionAsString("0.0.1");
//        flyway.setLocations(sqlLocation);
//        
//        // 需要修改Schema管理的表名，以区别不同应用/服务
//        flyway.setTable("_fs_schema_version");
//        
//        return flyway;
//    }
    
//    @Bean
//    AtlasTagProvider atlasCommonTags(@Value("${spring.application.name}") String appName) {
//        return () -> Collections.singletonMap("app", appName);
//    }
//
//    @Bean
//    public CommandLineRunner registerExtMetrics(Registry registry) {
//    	final Queue queue;
//        return new CommandLineRunner() {
//            @Override
//            public void run(String... strings) throws Exception {
//                Jmx.registerStandardMXBeans(registry);
//                Spectator.globalRegistry().add(registry);
//                GcLogger gc = new GcLogger();
//                gc.start(null);
//            }
//        };
//    }
}
