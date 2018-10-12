package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "mc_downsample")
public class McDownsample {
    @Id
    private String id;

    @Column(name = "system_id")
    private String systemId;

    @Column(name = "cq_name")
    private String cqName;

    private String remarks;

    @Column(name = "sample_sql")
    private String sampleSql;

    private String synced;

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
     * @return cq_name
     */
    public String getCqName() {
		return cqName;
	}

    /**
     * @param cq_name
     */
	public void setCqName(String cqName) {
		this.cqName = cqName;
	}

	/**
     * @return remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return sample_sql
     */
    public String getSampleSql() {
        return sampleSql;
    }

    /**
     * @param sampleSql
     */
    public void setSampleSql(String sampleSql) {
        this.sampleSql = sampleSql;
    }

    /**
     * @return synced
     */
    public String getSynced() {
        return synced;
    }

    /**
     * @param synced
     */
    public void setSynced(String synced) {
        this.synced = synced;
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