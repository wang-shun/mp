package com.fiberhome.mapps.intergration.error;

import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorPageConfig {
	@Bean
    public ErrorAttributes errorAttributes() {
		ErrorPageAttributes attr = new ErrorPageAttributes();
    	return attr;
    }
}
