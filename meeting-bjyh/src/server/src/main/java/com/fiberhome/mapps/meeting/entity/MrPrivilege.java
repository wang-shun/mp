package com.fiberhome.mapps.meeting.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "mr_privilege")
public class MrPrivilege
{
    @Id
    private String id;

    private String ecid;

    @Column(name = "room_id")
    private String roomId;

    /**
     * user：用户 dept：部门
     */
    private String type;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "authr_time")
    private Date   authrTime;

    @Column(name = "dept_order")
    private String deptOrder;

    @Column(name = "priv")
    private String priv;

    public String getPriv()
    {
        return priv;
    }

    public void setPriv(String priv)
    {
        this.priv = priv;
    }

    public String getDeptOrder()
    {
        return deptOrder;
    }

    public void setDeptOrder(String deptOrder)
    {
        this.deptOrder = deptOrder;
    }

    /**
     * @return id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return ecid
     */
    public String getEcid()
    {
        return ecid;
    }

    /**
     * @param ecid
     */
    public void setEcid(String ecid)
    {
        this.ecid = ecid;
    }

    /**
     * @return room_id
     */
    public String getRoomId()
    {
        return roomId;
    }

    /**
     * @param roomId
     */
    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

    /**
     * 获取user：用户 dept：部门
     *
     * @return type - user：用户 dept：部门
     */
    public String getType()
    {
        return type;
    }

    /**
     * 设置user：用户 dept：部门
     *
     * @param type user：用户 dept：部门
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * @return entity_id
     */
    public String getEntityId()
    {
        return entityId;
    }

    /**
     * @param entityId
     */
    public void setEntityId(String entityId)
    {
        this.entityId = entityId;
    }

    /**
     * @return entity_name
     */
    public String getEntityName()
    {
        return entityName;
    }

    /**
     * @param entityName
     */
    public void setEntityName(String entityName)
    {
        this.entityName = entityName;
    }

    /**
     * @return authr_time
     */
    public Date getAuthrTime()
    {
        return authrTime;
    }

    /**
     * @param authrTime
     */
    public void setAuthrTime(Date authrTime)
    {
        this.authrTime = authrTime;
    }
}