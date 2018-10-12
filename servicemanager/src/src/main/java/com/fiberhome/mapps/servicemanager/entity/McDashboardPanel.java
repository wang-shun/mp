package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "MC_DASHBOARD_PANEL")
public class McDashboardPanel {
    @Id
    private String id;

    @Column(name = "dashboard_id")
    private String dashboardId;

    @Column(name = "system_id")
    private String systemId;

    private String name;

    private Long sequ;

    /**
     * 1:����ͼ��2:����ͼ��3:�ѻ�ͼ��4:��ͼ��5:�Ǳ���
     */
    @Column(name = "chart_type")
    private String chartType;

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
     * @return sequ
     */
    public Long getSequ() {
        return sequ;
    }

    /**
     * @param sequ
     */
    public void setSequ(Long sequ) {
        this.sequ = sequ;
    }

    /**
     * ��ȡ1:����ͼ��2:����ͼ��3:�ѻ�ͼ��4:��ͼ��5:�Ǳ���
     *
     * @return chart_type - 1:����ͼ��2:����ͼ��3:�ѻ�ͼ��4:��ͼ��5:�Ǳ���
     */
    public String getChartType() {
        return chartType;
    }

    /**
     * ����1:����ͼ��2:����ͼ��3:�ѻ�ͼ��4:��ͼ��5:�Ǳ���
     *
     * @param chartType 1:����ͼ��2:����ͼ��3:�ѻ�ͼ��4:��ͼ��5:�Ǳ���
     */
    public void setChartType(String chartType) {
        this.chartType = chartType;
    }
}