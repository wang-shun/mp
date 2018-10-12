package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "MC_MEASUREMENT")
public class McMeasurement {
    @Id
    private String id;

    @Column(name = "system_id")
    private String systemId;

    private String measurement;

    private String name;

    /**
     * ����2h 3d 4w
     */
    @Column(name = "retain_time")
    private String retainTime;

    @Column(name = "retain_policy")
    private String retainPolicy;

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
     * ��ȡ����2h 3d 4w
     *
     * @return retain_time - ����2h 3d 4w
     */
    public String getRetainTime() {
        return retainTime;
    }

    /**
     * ���ñ���2h 3d 4w
     *
     * @param retainTime ����2h 3d 4w
     */
    public void setRetainTime(String retainTime) {
        this.retainTime = retainTime;
    }

    /**
     * @return retain_policy
     */
    public String getRetainPolicy() {
        return retainPolicy;
    }

    /**
     * @param retainPolicy
     */
    public void setRetainPolicy(String retainPolicy) {
        this.retainPolicy = retainPolicy;
    }
}