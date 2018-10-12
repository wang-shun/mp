package com.fiberhome.mapps.servicemanager.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "mc_alert_logs")
public class McAlertLogs {
    @Id
    private String id;

    @Column(name = "rule_id")
    private String ruleId;

    /**
     * info:��ʾ warn:���� critical:���� resetWarn:�������� resetCritical:�����������
     */
    @Column(name = "alert_level")
    private String alertLevel;

    private String message;

    /**
     * json
     */
    @Column(name = "alert_data")
    private String alertData;

    @Column(name = "alert_time")
    private Date alertTime;

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
     * ��ȡinfo:��ʾ warn:���� critical:���� resetWarn:�������� resetCritical:�����������
     *
     * @return level - info:��ʾ warn:���� critical:���� resetWarn:�������� resetCritical:�����������
     */
    public String getAlertLevel() {
        return alertLevel;
    }

    /**
     * ����info:��ʾ warn:���� critical:���� resetWarn:�������� resetCritical:�����������
     *
     * @param level info:��ʾ warn:���� critical:���� resetWarn:�������� resetCritical:�����������
     */
    public void setAlertLevel(String alertLevel) {
        this.alertLevel = alertLevel;
    }

    /**
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * ��ȡjson
     *
     * @return alert_data - json
     */
    public String getAlertData() {
        return alertData;
    }

    /**
     * ����json
     *
     * @param alertData json
     */
    public void setAlertData(String alertData) {
        this.alertData = alertData;
    }

    /**
     * @return alert_time
     */
    public Date getAlertTime() {
        return alertTime;
    }

    /**
     * @param alertTime
     */
    public void setAlertTime(Date alertTime) {
        this.alertTime = alertTime;
    }
}