package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "mc_retention_policy")
public class McRetentionPolicy {
    @Id
    private String id;

    @Column(name = "system_id")
    private String systemId;

    private String rp;

    @Column(name = "rp_name")
    private String rpName;

    /**
     * 比如2h 3d 4w
     */
    @Column(name = "retain_time")
    private String retainTime;

    @Column(name = "is_default")
    private String isDefault;

    private String enabled;

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

    /**
     * @return rp_name
     */
    public String getRpName() {
        return rpName;
    }

    /**
     * @param rpName
     */
    public void setRpName(String rpName) {
        this.rpName = rpName;
    }

    /**
     * 获取比如2h 3d 4w
     *
     * @return retain_time - 比如2h 3d 4w
     */
    public String getRetainTime() {
        return retainTime;
    }

    /**
     * 设置比如2h 3d 4w
     *
     * @param retainTime 比如2h 3d 4w
     */
    public void setRetainTime(String retainTime) {
        this.retainTime = retainTime;
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
}