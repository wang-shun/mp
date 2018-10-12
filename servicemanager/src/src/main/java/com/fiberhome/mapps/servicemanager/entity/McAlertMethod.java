package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "MC_ALERT_METHOD")
public class McAlertMethod {
    @Id
    private String id;

    @Column(name = "rule_id")
    private String ruleId;

    /**
     * email:�ʼ�,sms:����
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
     * ��ȡemail:�ʼ�,sms:����
     *
     * @return alert_method - email:�ʼ�,sms:����
     */
    public String getAlertMethod() {
        return alertMethod;
    }

    /**
     * ����email:�ʼ�,sms:����
     *
     * @param alertMethod email:�ʼ�,sms:����
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