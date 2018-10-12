package com.fiberhome.mapps.servicemanager.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

public class ClientDatabaseInfo {
	@Id
    private String id;

    @Column(name = "db_type")
    private String dbType;

    private String host;

    private Long port;

    private String sid;

    @Column(name = "db_name")
    private String dbName;

    @Column(name = "user_name")
    private String userName;

    private String password;

    @Column(name = "create_time")
    private Date createTime;

    private String creator;

    private String enabled;
    
    private Long appnums;
    
    private String remarks;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Long getPort() {
		return port;
	}

	public void setPort(Long port) {
		this.port = port;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public Long getAppnums() {
		return appnums;
	}

	public void setAppnums(Long appnums) {
		this.appnums = appnums;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
