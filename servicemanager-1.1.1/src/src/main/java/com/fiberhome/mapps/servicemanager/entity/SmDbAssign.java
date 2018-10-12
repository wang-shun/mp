package com.fiberhome.mapps.servicemanager.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sm_db_assign")
public class SmDbAssign {
    /**
     * 数据库ID
     */
    @Id
    @Column(name = "db_id")
    private String dbId;

    /**
     * 服务ID
     */
    @Id
    @Column(name = "svc_id")
    private String svcId;

    /**
     * 资源分配ID
     */
    @Column(name = "assign_id")
    private String assignId;

    /**
     * 分配时间
     */
    @Column(name = "assign_time")
    private Date assignTime;

    /**
     * 获取数据库ID
     *
     * @return db_id - 数据库ID
     */
    public String getDbId() {
        return dbId;
    }

    /**
     * 设置数据库ID
     *
     * @param dbId 数据库ID
     */
    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    /**
     * 获取服务ID
     *
     * @return svc_id - 服务ID
     */
    public String getSvcId() {
        return svcId;
    }

    /**
     * 设置服务ID
     *
     * @param svcId 服务ID
     */
    public void setSvcId(String svcId) {
        this.svcId = svcId;
    }

    /**
     * 获取资源分配ID
     *
     * @return assign_id - 资源分配ID
     */
    public String getAssignId() {
        return assignId;
    }

    /**
     * 设置资源分配ID
     *
     * @param assignId 资源分配ID
     */
    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    /**
     * 获取分配时间
     *
     * @return assign_time - 分配时间
     */
    public Date getAssignTime() {
        return assignTime;
    }

    /**
     * 设置分配时间
     *
     * @param assignTime 分配时间
     */
    public void setAssignTime(Date assignTime) {
        this.assignTime = assignTime;
    }
}