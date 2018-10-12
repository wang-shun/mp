package com.fiberhome.mapps.survey.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "su_participants")
public class SuParticipants
{
    @Id
    private String id;

    /**
     * �μӶ�������
     */
    private String type;

    /**
     * ������Ա˵�μӵĶ���ID
     */
    @Column(name = "object_id")
    private String objectId;

    /**
     * ������Ա˵�μӵĶ������
     */
    @Column(name = "object_name")
    private String objectName;

    /**
     * user���û� dept������
     */
    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "entity_name")
    private String entityName;

    /**
     * ˵ѡ���ŵ�order������Ա���ڲ��ŵ�order
     */
    @Column(name = "dept_order")
    private String deptOrder;

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
     * ��ȡ�μӶ�������
     *
     * @return type - �μӶ�������
     */
    public String getType()
    {
        return type;
    }

    /**
     * ���òμӶ�������
     *
     * @param type
     *            �μӶ�������
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * ��ȡ������Ա˵�μӵĶ���ID
     *
     * @return object_id - ������Ա˵�μӵĶ���ID
     */
    public String getObjectId()
    {
        return objectId;
    }

    /**
     * ���ò�����Ա˵�μӵĶ���ID
     *
     * @param objectId
     *            ������Ա˵�μӵĶ���ID
     */
    public void setObjectId(String objectId)
    {
        this.objectId = objectId;
    }

    /**
     * ��ȡ������Ա˵�μӵĶ������
     *
     * @return object_name - ������Ա˵�μӵĶ������
     */
    public String getObjectName()
    {
        return objectName;
    }

    /**
     * ���ò�����Ա˵�μӵĶ������
     *
     * @param objectName
     *            ������Ա˵�μӵĶ������
     */
    public void setObjectName(String objectName)
    {
        this.objectName = objectName;
    }

    /**
     * ��ȡuser���û� dept������
     *
     * @return entity_type - user���û� dept������
     */
    public String getEntityType()
    {
        return entityType;
    }

    /**
     * ����user���û� dept������
     *
     * @param entityType
     *            user���û� dept������
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
     * ��ȡ˵ѡ���ŵ�order������Ա���ڲ��ŵ�order
     *
     * @return dept_order - ˵ѡ���ŵ�order������Ա���ڲ��ŵ�order
     */
    public String getDeptOrder()
    {
        return deptOrder;
    }

    /**
     * ����˵ѡ���ŵ�order������Ա���ڲ��ŵ�order
     *
     * @param deptOrder
     *            ˵ѡ���ŵ�order������Ա���ڲ��ŵ�order
     */
    public void setDeptOrder(String deptOrder)
    {
        this.deptOrder = deptOrder;
    }
}