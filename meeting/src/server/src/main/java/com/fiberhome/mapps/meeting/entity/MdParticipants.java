package com.fiberhome.mapps.meeting.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "MD_PARTICIPANTS")
public class MdParticipants
{
    @Id
    private String id;

    /**
     * 参加对象类型
     */
    private String type;

    /**
     * 参与人员说参加的对象ID
     */
    @Column(name = "object_id")
    private String objectId;

    /**
     * 参与人员说参加的对象名称
     */
    @Column(name = "object_name")
    private String objectName;

    /**
     * user：用户 dept：部门
     */
    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "dept_order")
    private String deptOrder;

    private String deptName;

    private String phone;
    
    @Column(name = "parent_id")
    private String parentId;

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
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
     * 获取参加对象类型
     *
     * @return type - 参加对象类型
     */
    public String getType()
    {
        return type;
    }

    /**
     * 设置参加对象类型
     *
     * @param type 参加对象类型
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * 获取参与人员说参加的对象ID
     *
     * @return object_id - 参与人员说参加的对象ID
     */
    public String getObjectId()
    {
        return objectId;
    }

    /**
     * 设置参与人员说参加的对象ID
     *
     * @param objectId 参与人员说参加的对象ID
     */
    public void setObjectId(String objectId)
    {
        this.objectId = objectId;
    }

    /**
     * 获取参与人员说参加的对象名称
     *
     * @return object_name - 参与人员说参加的对象名称
     */
    public String getObjectName()
    {
        return objectName;
    }

    /**
     * 设置参与人员说参加的对象名称
     *
     * @param objectName 参与人员说参加的对象名称
     */
    public void setObjectName(String objectName)
    {
        this.objectName = objectName;
    }

    /**
     * 获取user：用户 dept：部门
     *
     * @return entity_type - user：用户 dept：部门
     */
    public String getEntityType()
    {
        return entityType;
    }

    /**
     * 设置user：用户 dept：部门
     *
     * @param entityType user：用户 dept：部门
     */
    public void setEntityType(String entityType)
    {
        this.entityType = entityType;
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
     * @return dept_order
     */
    public String getDeptOrder()
    {
        return deptOrder;
    }

    /**
     * @param deptOrder
     */
    public void setDeptOrder(String deptOrder)
    {
        this.deptOrder = deptOrder;
    }

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}