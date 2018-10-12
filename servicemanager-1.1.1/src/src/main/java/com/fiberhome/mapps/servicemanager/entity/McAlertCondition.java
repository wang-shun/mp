package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "mc_alert_condition")
public class McAlertCondition {
    @Id
    private String id;

    @Column(name = "rule_id")
    private String ruleId;

    /**
     * threshold:��ֵ relative:��Ա仯 deadman:������  
     */
    @Column(name = "rule_type")
    private String ruleType;

    /**
     * info:��ʾ warn:���� critical:���� resetWarn:�������� resetCritical:�����������
     */
    @Column(name = "alert_level")
    private String alertLevel;

    @Column(name = "setting_1")
    private String setting1;

    @Column(name = "setting_2")
    private String setting2;

    @Column(name = "setting_3")
    private String setting3;

    @Column(name = "setting_4")
    private String setting4;

    @Column(name = "setting_5")
    private String setting5;

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
     * ��ȡthreshold:��ֵ relative:��Ա仯 deadman:������  
     *
     * @return rule_type - threshold:��ֵ relative:��Ա仯 deadman:������  
     */
    public String getRuleType() {
        return ruleType;
    }

    /**
     * ����threshold:��ֵ relative:��Ա仯 deadman:������  
     *
     * @param ruleType threshold:��ֵ relative:��Ա仯 deadman:������  
     */
    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
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
     * @return setting_1
     */
    public String getSetting1() {
        return setting1;
    }

    /**
     * @param setting1
     */
    public void setSetting1(String setting1) {
        this.setting1 = setting1;
    }

    /**
     * @return setting_2
     */
    public String getSetting2() {
        return setting2;
    }

    /**
     * @param setting2
     */
    public void setSetting2(String setting2) {
        this.setting2 = setting2;
    }

    /**
     * @return setting_3
     */
    public String getSetting3() {
        return setting3;
    }

    /**
     * @param setting3
     */
    public void setSetting3(String setting3) {
        this.setting3 = setting3;
    }

    /**
     * @return setting_4
     */
    public String getSetting4() {
        return setting4;
    }

    /**
     * @param setting4
     */
    public void setSetting4(String setting4) {
        this.setting4 = setting4;
    }

    /**
     * @return setting_5
     */
    public String getSetting5() {
        return setting5;
    }

    /**
     * @param setting5
     */
    public void setSetting5(String setting5) {
        this.setting5 = setting5;
    }
}