package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "mc_system")
public class McSystem {
    @Id
    private String id;

    private String db;

    @Column(name = "db_user")
    private String dbUser;

    @Column(name = "db_passwd")
    private String dbPasswd;

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
     * @return db
     */
    public String getDb() {
        return db;
    }

    /**
     * @param db
     */
    public void setDb(String db) {
        this.db = db;
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
     * @return db_user
     */
	public String getDbUser() {
		return dbUser;
	}
	
	/**
     * @param db_user
     */
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	/**
     * @return db_passwd
     */
	public String getDbPasswd() {
		return dbPasswd;
	}

	/**
     * @param db_passwd
     */
	public void setDbPasswd(String dbPasswd) {
		this.dbPasswd = dbPasswd;
	}
}