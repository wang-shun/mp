package com.fiberhome.mapps.servicemanager.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "SM_DB_ASSIGN")
public class SmDbAssign {
    /**
     * ���ݿ�ID
     */
    @Id
    @Column(name = "db_id")
    private String dbId;

    /**
     * ����ID
     */
    @Id
    @Column(name = "svc_id")
    private String svcId;

    /**
     * ��Դ����ID
     */
    @Column(name = "assign_id")
    private String assignId;

    /**
     * ����ʱ��
     */
    @Column(name = "assign_time")
    private Date assignTime;

    /**
     * ��ȡ���ݿ�ID
     *
     * @return db_id - ���ݿ�ID
     */
    public String getDbId() {
        return dbId;
    }

    /**
     * �������ݿ�ID
     *
     * @param dbId ���ݿ�ID
     */
    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    /**
     * ��ȡ����ID
     *
     * @return svc_id - ����ID
     */
    public String getSvcId() {
        return svcId;
    }

    /**
     * ���÷���ID
     *
     * @param svcId ����ID
     */
    public void setSvcId(String svcId) {
        this.svcId = svcId;
    }

    /**
     * ��ȡ��Դ����ID
     *
     * @return assign_id - ��Դ����ID
     */
    public String getAssignId() {
        return assignId;
    }

    /**
     * ������Դ����ID
     *
     * @param assignId ��Դ����ID
     */
    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    /**
     * ��ȡ����ʱ��
     *
     * @return assign_time - ����ʱ��
     */
    public Date getAssignTime() {
        return assignTime;
    }

    /**
     * ���÷���ʱ��
     *
     * @param assignTime ����ʱ��
     */
    public void setAssignTime(Date assignTime) {
        this.assignTime = assignTime;
    }
}