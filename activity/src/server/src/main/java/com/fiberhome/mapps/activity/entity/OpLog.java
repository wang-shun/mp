package com.fiberhome.mapps.activity.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "op_log")
public class OpLog
{
    @Id
    private String id;

    /**
     * 代码package
     */
    private String module;

    private String object;

    private String op;

    private String content;

    /**
     * Y：成功， N：失败
     */
    private String result;

    private String ecid;

    @Column(name = "dept_name")
    private String deptName;

    @Column(name = "dept_id")
    private String deptId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "op_time")
    private Date   opTime;

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
     * 获取代码package
     *
     * @return module - 代码package
     */
    public String getModule()
    {
        return module;
    }

    /**
     * 设置代码package
     *
     * @param module 代码package
     */
    public void setModule(String module)
    {
        this.module = module;
    }

    /**
     * @return object
     */
    public String getObject()
    {
        return object;
    }

    /**
     * @param object
     */
    public void setObject(String object)
    {
        this.object = object;
    }

    /**
     * @return op
     */
    public String getOp()
    {
        return op;
    }

    /**
     * @param op
     */
    public void setOp(String op)
    {
        this.op = op;
    }

    /**
     * @return content
     */
    public String getContent()
    {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content)
    {
        this.content = content;
    }

    /**
     * 获取Y：成功， N：失败
     *
     * @return result - Y：成功， N：失败
     */
    public String getResult()
    {
        return result;
    }

    /**
     * 设置Y：成功， N：失败
     *
     * @param result Y：成功， N：失败
     */
    public void setResult(String result)
    {
        this.result = result;
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
     * @return dept_name
     */
    public String getDeptName()
    {
        return deptName;
    }

    /**
     * @param deptName
     */
    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    /**
     * @return dept_id
     */
    public String getDeptId()
    {
        return deptId;
    }

    /**
     * @param deptId
     */
    public void setDeptId(String deptId)
    {
        this.deptId = deptId;
    }

    /**
     * @return user_name
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    /**
     * @return user_id
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * @return op_time
     */
    public Date getOpTime()
    {
        return opTime;
    }

    /**
     * @param opTime
     */
    public void setOpTime(Date opTime)
    {
        this.opTime = opTime;
    }
}