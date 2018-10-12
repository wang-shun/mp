package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "MC_DASHBOARD")
public class McDashboard {
    @Id
    private String id;

    private String name;

    /**
     * json��ʽ���ֶ�������
     */
    private String layout;

    @Column(name = "is_default")
    private String isDefault;

    @Column(name = "refresh_time")
    private String refreshTime;

    @Column(name = "time_range")
    private String timeRange;

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
     * ��ȡjson��ʽ���ֶ�������
     *
     * @return layout - json��ʽ���ֶ�������
     */
    public String getLayout() {
        return layout;
    }

    /**
     * ����json��ʽ���ֶ�������
     *
     * @param layout json��ʽ���ֶ�������
     */
    public void setLayout(String layout) {
        this.layout = layout;
    }

    /**
     * @return is_default
     */
    public String getIsDefault() {
        return isDefault;
    }

    /**
     * @param isDefault
     */
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * @return refresh_time
     */
    public String getRefreshTime() {
        return refreshTime;
    }

    /**
     * @param refreshTime
     */
    public void setRefreshTime(String refreshTime) {
        this.refreshTime = refreshTime;
    }

    /**
     * @return time_range
     */
    public String getTimeRange() {
        return timeRange;
    }

    /**
     * @param timeRange
     */
    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }
}