package com.fiberhome.mapps.servicemanager.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "SM_REDIS")
public class SmRedis {
    @Id
    private String id;

    private String remarks;

    private String host;

    private Long port;

    /**
     * Redis数据库索引（默认为0）
     */
    @Column(name = "db_index")
    private Long dbIndex;

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
     * 获取Redis数据库索引（默认为0）
     *
     * @return db_index - Redis数据库索引（默认为0）
     */
    public Long getDbIndex() {
        return dbIndex;
    }

    /**
     * 设置Redis数据库索引（默认为0）
     *
     * @param dbIndex Redis数据库索引（默认为0）
     */
    public void setDbIndex(Long dbIndex) {
        this.dbIndex = dbIndex;
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
}