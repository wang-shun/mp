package com.fiberhome.mapps.gateway.utils;

import java.util.Map;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

public class YamlUtils {
    private static final Logger logger = LogManager.getLogger(YamlUtils.class);

    public static Map<String, Object> yaml2Map(String yamlSource) {
        try {
            YamlMapFactoryBean yaml = new YamlMapFactoryBean();
            yaml.setResources(new ClassPathResource(yamlSource));
            return yaml.getObject();
        } catch (Exception e) {
            logger.error("Cannot read yaml", e);
            return null;
        }
    }

    public static Properties yaml2Properties(String yamlSource) {
        try {
            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(new ClassPathResource(yamlSource));
            return yaml.getObject();
        } catch (Exception e) {
            logger.error("Cannot read yaml", e);
            return null;
        }
    }
}
