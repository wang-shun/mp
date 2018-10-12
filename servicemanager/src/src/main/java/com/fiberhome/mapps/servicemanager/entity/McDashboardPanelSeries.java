package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "MC_DASHBOARD_PANEL_SERIES")
public class McDashboardPanelSeries {
    @Id
    private String id;

    @Column(name = "panel_id")
    private String panelId;

    @Column(name = "dashboard_id")
    private String dashboardId;

    @Column(name = "retention_policy")
    private String retentionPolicy;

    private String measurement;

    /**
     * json
     */
    @Column(name = "where_setting")
    private String whereSetting;

    /**
     * json
     */
    @Column(name = "fields_setting")
    private String fieldsSetting;

    private String sql;

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
     * @return panel_id
     */
    public String getPanelId() {
        return panelId;
    }

    /**
     * @param panelId
     */
    public void setPanelId(String panelId) {
        this.panelId = panelId;
    }

    /**
     * @return dashboard_id
     */
    public String getDashboardId() {
        return dashboardId;
    }

    /**
     * @param dashboardId
     */
    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    /**
     * @return retention_policy
     */
    public String getRetentionPolicy() {
        return retentionPolicy;
    }

    /**
     * @param retentionPolicy
     */
    public void setRetentionPolicy(String retentionPolicy) {
        this.retentionPolicy = retentionPolicy;
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
     * ��ȡjson
     *
     * @return where_setting - json
     */
    public String getWhereSetting() {
        return whereSetting;
    }

    /**
     * ����json
     *
     * @param whereSetting json
     */
    public void setWhereSetting(String whereSetting) {
        this.whereSetting = whereSetting;
    }

    /**
     * ��ȡjson
     *
     * @return fields_setting - json
     */
    public String getFieldsSetting() {
        return fieldsSetting;
    }

    /**
     * ����json
     *
     * @param fieldsSetting json
     */
    public void setFieldsSetting(String fieldsSetting) {
        this.fieldsSetting = fieldsSetting;
    }

    /**
     * @return sql
     */
    public String getSql() {
        return sql;
    }

    /**
     * @param sql
     */
    public void setSql(String sql) {
        this.sql = sql;
    }
}