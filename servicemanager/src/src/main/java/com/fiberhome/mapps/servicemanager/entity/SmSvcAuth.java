package com.fiberhome.mapps.servicemanager.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "SM_SVC_AUTH")
public class SmSvcAuth {
    @Id
    @Column(name = "app_id")
    private String appId;

    @Id
    @Column(name = "svc_id")
    private String svcId;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "svc_name")
    private String svcName;

    /**
     * UUID,去除-
     */
    private String appkey;

    /**
     * 加密存储
     */
    private String secret;

    @Column(name = "expired_time")
    private Date expiredTime;

    @Column(name = "auth_time")
    private Date authTime;

    @Column(name = "route_id")
    private String routeId;

    /**
     * @return app_id
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return svc_id
     */
    public String getSvcId() {
        return svcId;
    }

    /**
     * @param svcId
     */
    public void setSvcId(String svcId) {
        this.svcId = svcId;
    }

    /**
     * @return app_name
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * @return svc_name
     */
    public String getSvcName() {
        return svcName;
    }

    /**
     * @param svcName
     */
    public void setSvcName(String svcName) {
        this.svcName = svcName;
    }

    /**
     * 获取UUID,去除-
     *
     * @return appkey - UUID,去除-
     */
    public String getAppkey() {
        return appkey;
    }

    /**
     * 设置UUID,去除-
     *
     * @param appkey UUID,去除-
     */
    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    /**
     * 获取加密存储
     *
     * @return secret - 加密存储
     */
    public String getSecret() {
        return secret;
    }

    /**
     * 设置加密存储
     *
     * @param secret 加密存储
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * @return expired_time
     */
    public Date getExpiredTime() {
        return expiredTime;
    }

    /**
     * @param expiredTime
     */
    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    /**
     * @return auth_time
     */
    public Date getAuthTime() {
        return authTime;
    }

    /**
     * @param authTime
     */
    public void setAuthTime(Date authTime) {
        this.authTime = authTime;
    }

    /**
     * @return route_id
     */
    public String getRouteId() {
        return routeId;
    }

    /**
     * @param routeId
     */
    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }
}