package com.fiberhome.mapps.servicemanager.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "SM_DATABASE")
public class SmDatabase {
    @Id
    private String id;

    /**
     * postgresqloracle
     */
    @Column(name = "db_type")
    private String dbType;

    private String host;

    private Long port;

    private String sid;

    @Column(name = "db_name")
    private String dbName;

    @Column(name = "user_name")
    private String userName;

    /**
     * 加密后的密码
     */
    private String password;

    @Column(name = "create_time")
    private Date createTime;

    private String creator;

    /**
     * 启用状态
     */
    private String enabled;

    private String remarks;

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
     * 获取postgresqloracle
     *
     * @return db_type - postgresqloracle
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * 设置postgresqloracle
     *
     * @param dbType postgresqloracle
     */
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    /**
     * @return host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return port
     */
    public Long getPort() {
        return port;
    }

    /**
     * @param port
     */
    public void setPort(Long port) {
        this.port = port;
    }

    /**
     * @return sid
     */
    public String getSid() {
        return sid;
    }

    /**
     * @param sid
     */
    public void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * @return db_name
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * @param dbName
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * @return user_name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取加密后的密码
     *
     * @return password - 加密后的密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置加密后的密码
     *
     * @param password 加密后的密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取启用状态
     *
     * @return enabled - 启用状态
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * 设置启用状态
     *
     * @param enabled 启用状态
     */
    public void setEnabled(String enabled) {
        this.enabled = enabled;
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
}