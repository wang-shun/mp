package com.fiberhome.mapps.servicemanager.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sm_resource")
public class SmResource {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 资源ID
     */
    @Column(name = "res_id")
    private String resId;

    /**
     * 服务ID
     */
    private String name;

    private String remarks;

    private String creator;

    @Column(name = "create_time")
    private Date createTime;

    private String modifier;

    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 启用状态
     */
    private String enabled;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取资源ID
     *
     * @return res_id - 资源ID
     */
    public String getResId() {
        return resId;
    }

    /**
     * 设置资源ID
     *
     * @param resId 资源ID
     */
    public void setResId(String resId) {
        this.resId = resId;
    }

    /**
     * 获取服务ID
     *
     * @return name - 服务ID
     */
    public String getName() {
        return name;
    }

    /**
     * 设置服务ID
     *
     * @param name 服务ID
     */
    public void setName(String name) {
        this.name = name;
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
     * @return modifier
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * @param modifier
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * @return modify_time
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
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