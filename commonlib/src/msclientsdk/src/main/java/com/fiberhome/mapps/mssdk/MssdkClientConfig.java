package com.fiberhome.mapps.mssdk;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * sdk配置入口类，用于扫描com.fiberhome.mapps.mssdk package下的配置
 * @author fh
 *
 */
@Configuration
@ComponentScan
@ConditionalOnProperty(name = "eureka.client.enabled", matchIfMissing = true)
public class MssdkClientConfig {
    public MssdkClientConfig() {
    	System.out.println("MssdkClientConfig has been scanned.");
    }
}
