package com.fiberhome.mapps.contact;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan
@EnableAutoConfiguration()
@SpringBootApplication
@EnableScheduling
public class TestApplication {
	public static void main(String[] args) {
        new SpringApplicationBuilder(TestApplication.class).web(true).run(args);
    }
	
	
}
