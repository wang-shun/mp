package com.fiberhome.mapps.zipkinserver;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;

import zipkin.server.EnableZipkinServer;

@Configuration
@EnableZipkinServer
@SpringBootApplication
public class ZipkinServerApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ZipkinServerApplication.class).web(true).run(args);
	}
}
