package com.fiberhome.mapps.activity.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "AT_PRIVILEGE")
public class AtPrivilege {
    @Id
    private String id;

    private String ecid;

    @Column(name = "act_id")
    private String actId;

    /**
     * user���û� dept������
     */
    private String type;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "authr_time")
    private Date authrTime;

    /**
     * ��Ȩ����Ϊ����ʱ���������ڵ���ţ����ڲ�ѯ�Ӳ���Ȩ��
     */
    @Column(name = "dept_order")
    private String deptOrder;

    /**
     * admin:����Ա service:������Ա user:��ͨ�û�
     */
    private String priv;

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
     * @return ecid
     */
    public String getEcid() {
        return ecid;
    }

    /**
     * @param ecid
     */
    public void setEcid(String ecid) {
        this.ecid = ecid;
    }

    /**
     * @return act_id
     */
    public String getActId() {
        return actId;
    }

    /**
     * @param actId
     */
    public void setActId(String actId) {
        this.actId = actId;
    }

    /**
     * ��ȡuser���û� dept������
     *
     * @return type - user���û� dept������
     */
    public String getType() {
        return type;
    }

    /**
     * ����user���û� dept������
     *
     * @param type user���û� dept������
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return entity_id
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     */
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    /**
     * @return entity_name
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @param entityName
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * @return authr_time
     */
    public Date getAuthrTime() {
        return authrTime;
    }

    /**
     * @param authrTime
     */
    public void setAuthrTime(Date authrTime) {
        this.authrTime = authrTime;
    }

    /**
     * ��ȡ��Ȩ����Ϊ����ʱ���������ڵ���ţ����ڲ�ѯ�Ӳ���Ȩ��
     *
     * @return dept_order - ��Ȩ����Ϊ����ʱ���������ڵ���ţ����ڲ�ѯ�Ӳ���Ȩ��
     */
    public String getDeptOrder() {
        return deptOrder;
    }

    /**
     * ������Ȩ����Ϊ����ʱ���������ڵ���ţ����ڲ�ѯ�Ӳ���Ȩ��
     *
     * @param deptOrder ��Ȩ����Ϊ����ʱ���������ڵ���ţ����ڲ�ѯ�Ӳ���Ȩ��
     */
    public void setDeptOrder(String deptOrder) {
        this.deptOrder = deptOrder;
    }

    /**
     * ��ȡadmin:����Ա service:������Ա user:��ͨ�û�
     *
     * @return priv - admin:����Ա service:������Ա user:��ͨ�û�
     */
    public String getPriv() {
        return priv;
    }

    /**
     * ����admin:����Ա service:������Ա user:��ͨ�û�
     *
     * @param priv admin:����Ա service:������Ա user:��ͨ�û�
     */
    public void setPriv(String priv) {
        this.priv = priv;
    }
}