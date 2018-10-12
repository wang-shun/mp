package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "sm_route")
public class SmRoute {
    @Id
    private String id;

    /**
     * 服务对应的ServiceId
     */
    @Column(name = "service_id")
    private String serviceId;

    /**
     * 服务对应的访问路径
     */
    private String url;

    @Column(name = "service_name")
    private String serviceName;

    private String path;

    @Column(name = "strip_prefix")
    private String stripPrefix;

    private String retryable;

    /**
     * 逗号分隔
     */
    @Column(name = "sensitive_headers")
    private String sensitiveHeaders;

    @Column(name = "custom_policy")
    private String customPolicy;

    /**
     * 启用状态
     */
    private String enabled;

    @Column(name = "need_auth")
    private String needAuth;

    @Column(name = "auth_resource")
    private String authResource;

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
     * 获取服务对应的ServiceId
     *
     * @return service_id - 服务对应的ServiceId
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * 设置服务对应的ServiceId
     *
     * @param serviceId 服务对应的ServiceId
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * 获取服务对应的访问路径
     *
     * @return url - 服务对应的访问路径
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置服务对应的访问路径
     *
     * @param url 服务对应的访问路径
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return service_name
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return strip_prefix
     */
    public String getStripPrefix() {
        return stripPrefix;
    }

    /**
     * @param stripPrefix
     */
    public void setStripPrefix(String stripPrefix) {
        this.stripPrefix = stripPrefix;
    }

    /**
     * @return retryable
     */
    public String getRetryable() {
        return retryable;
    }

    /**
     * @param retryable
     */
    public void setRetryable(String retryable) {
        this.retryable = retryable;
    }

    /**
     * 获取逗号分隔
     *
     * @return sensitive_headers - 逗号分隔
     */
    public String getSensitiveHeaders() {
        return sensitiveHeaders;
    }

    /**
     * 设置逗号分隔
     *
     * @param sensitiveHeaders 逗号分隔
     */
    public void setSensitiveHeaders(String sensitiveHeaders) {
        this.sensitiveHeaders = sensitiveHeaders;
    }

    /**
     * @return custom_policy
     */
    public String getCustomPolicy() {
        return customPolicy;
    }

    /**
     * @param customPolicy
     */
    public void setCustomPolicy(String customPolicy) {
        this.customPolicy = customPolicy;
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
     * @return need_auth
     */
    public String getNeedAuth() {
        return needAuth;
    }

    /**
     * @param needAuth
     */
    public void setNeedAuth(String needAuth) {
        this.needAuth = needAuth;
    }

    /**
     * @return auth_resource
     */
    public String getAuthResource() {
        return authResource;
    }

    /**
     * @param authResource
     */
    public void setAuthResource(String authResource) {
        this.authResource = authResource;
    }
}