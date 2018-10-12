package com.fiberhome.mapps.mssdk.metrics;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * 指标信息
 * @author Administrator
 *
 */
public class MetricName {
    /**
     * 指标代码
     */
    private String measurement;
    
    /**
     * 指标tag
     */
    private Map<String, String> tags = new HashMap<>();
    
    public MetricName(String measurement) {
        this(measurement, null);
    }

    public MetricName(String measurement, Map<String, String> tags) {    
        this.measurement = measurement;
        if (tags != null) {
            tags.putAll(tags);
        }
    }
    
    /**
     * 设置tag
     * @param key tag key
     * @param value tag Value
     * @return 当前MetricName
     */
    public MetricName tag(String key, String value) {
        this.tags.put(key, value);
        return this;
    }

    /**
     * 获取指标代码
     * @return
     */
    public String getMeasurement() {
        return measurement;
    }

    /**
     * 设置指标代码
     * @param measurement
     */
    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    /**
     * 获取所有的tag信息
     * @return
     */
    public Map<String, String> getTags() {
        return tags;
    }

    /**
     * 设置tag信息
     * @param tags
     */
    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    /**
     * 获取通过指标代码和tag拼接的id
     * @return
     */
    public String getId() {
        return measurement + "," + StringUtils.collectionToCommaDelimitedString(tags.values());
    }
    
}
