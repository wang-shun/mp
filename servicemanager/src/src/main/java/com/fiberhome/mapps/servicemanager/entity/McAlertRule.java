package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "MC_ALERT_RULE")
public class McAlertRule {
    @Id
    private String id;

    @Column(name = "system_id")
    private String systemId;

    private String name;

    @Column(name = "past_time")
    private String pastTime;

    private String measurement;

    @Column(name = "value_field")
    private String valueField;

    private String message;

    private String enabled;

    private String func;

    @Column(name = "group_by")
    private String groupBy;

    @Column(name = "wh_ere")
    private String whEre;

    @Column(name = "query_ql")
    private String queryQl;

    private String rp;

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
     * @return system_id
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * @param systemId
     */
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return past_time
     */
    public String getPastTime() {
        return pastTime;
    }

    /**
     * @param pastTime
     */
    public void setPastTime(String pastTime) {
        this.pastTime = pastTime;
    }

    /**
     * @return measurement
     */
    public String getMeasurement() {
        return measurement;
    }

    /**
     * @param measurement
     */
    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    /**
     * @return value_field
     */
    public String getValueField() {
        return valueField;
    }

    /**
     * @param valueField
     */
    public void setValueField(String valueField) {
        this.valueField = valueField;
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
     * @return enabled
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * @param enabled
     */
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    /**
     * @return func
     */
    public String getFunc() {
        return func;
    }

    /**
     * @param func
     */
    public void setFunc(String func) {
        this.func = func;
    }

    /**
     * @return group_by
     */
    public String getGroupBy() {
        return groupBy;
    }

    /**
     * @param groupBy
     */
    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    /**
     * @return wh_ere
     */
    public String getWhEre() {
        return whEre;
    }

    /**
     * @param whEre
     */
    public void setWhEre(String whEre) {
        this.whEre = whEre;
    }

    /**
     * @return query_ql
     */
    public String getQueryQl() {
        return queryQl;
    }

    /**
     * @param queryQl
     */
    public void setQueryQl(String queryQl) {
        this.queryQl = queryQl;
    }

    /**
     * @return rp
     */
    public String getRp() {
        return rp;
    }

    /**
     * @param rp
     */
    public void setRp(String rp) {
        this.rp = rp;
    }
}