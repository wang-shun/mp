package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "mc_alert_method")
public class McAlertMethod {
    @Id
    private String id;

    @Column(name = "rule_id")
    private String ruleId;

    /**
     * email:邮件,sms:短信
     */
    @Column(name = "alert_method")
    private String alertMethod;

    private String config;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return rule_id
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * @param ruleId
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * 获取email:邮件,sms:短信
     *
     * @return alert_method - email:邮件,sms:短信
     */
    public String getAlertMethod() {
        return alertMethod;
    }

    /**
     * 设置email:邮件,sms:短信
     *
     * @param alertMethod email:邮件,sms:短信
     */
    public void setAlertMethod(String alertMethod) {
        this.alertMethod = alertMethod;
    }

    /**
     * @return config
     */
    public String getConfig() {
        return config;
    }

    /**
     * @param config
     */
    public void setConfig(String config) {
        this.config = config;
    }
}