package com.fiberhome.mapps.servicemanager.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "SM_RESOURCE")
public class SmResource {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * ��ԴID
     */
    @Column(name = "res_id")
    private String resId;

    /**
     * ����ID
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
     * ����״̬
     */
    private String enabled;

    /**
     * ��ȡID
     *
     * @return id - ID
     */
    public String getId() {
        return id;
    }

    /**
     * ����ID
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * ��ȡ��ԴID
     *
     * @return res_id - ��ԴID
     */
    public String getResId() {
        return resId;
    }

    /**
     * ������ԴID
     *
     * @param resId ��ԴID
     */
    public void setResId(String resId) {
        this.resId = resId;
    }

    /**
     * ��ȡ����ID
     *
     * @return name - ����ID
     */
    public String getName() {
        return name;
    }

    /**
     * ���÷���ID
     *
     * @param name ����ID
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
     * ��ȡ����״̬
     *
     * @return enabled - ����״̬
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * ��������״̬
     *
     * @param enabled ����״̬
     */
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
}