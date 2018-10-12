package com.fiberhome.mapps.meetingroom.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fiberhome.mapps.intergration.mybatis.OrgDept;

@OrgDept(orgDepId = "orgDeptId", orgDeptOrder = "orgDeptOrder")
@Table(name = "MR_RESERVED")
public class MrReserved
{
    @Id
    private String  id;

    private String  ecid;

    @Column(name = "room_id")
    private String  roomId;

    @Column(name = "reserved_user_id")
    private String  reservedUserId;

    @Column(name = "reserved_user_name")
    private String  reservedUserName;

    @Column(name = "reserved_user_dept")
    private String  reservedUserDept;

    @Column(name = "reserved_time")
    private Date    reservedTime;

    @Column(name = "order_time_begin")
    private Date    orderTimeBegin;

    @Column(name = "order_time_end")
    private Date    orderTimeEnd;

    @Column(name = "order_duration")
    private Long    orderDuration;

    @Column(name = "meeting_name")
    private String  meetingName;

    /**
     * 1-准备中 2-使用中 3-已结束 4-已取消 0-已删除
     */
    private String  status;

    @Column(name = "reserved_user_dept_id")
    private String  reservedUserDeptId;

    @Column(name = "need_approve")
    private String  needApprove;
    @Column(name = "remarks")
    private String  reservedRemark;

    @Column(name = "org_dept_id")
    private String  orgDeptId;         // 所属部门机构id
    @Column(name = "org_dept_order")
    private String  orgDeptOrder;      // 所属部门机构order
    @Column(name = "participants_num")
    private Integer participantsNum;
    @Column(name = "del_flag")
    private String  delFlag;
    @Column(name = "res_remark")
    private String  resRemark;

    public String getResRemark()
    {
        return resRemark;
    }

    public void setResRemark(String resRemark)
    {
        this.resRemark = resRemark;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public Integer getParticipantsNum()
    {
        return participantsNum;
    }

    public void setParticipantsNum(Integer participantsNum)
    {
        this.participantsNum = participantsNum;
    }

    public String getOrgDeptId()
    {
        return orgDeptId;
    }

    public void setOrgDeptId(String orgDeptId)
    {
        this.orgDeptId = orgDeptId;
    }

    public String getOrgDeptOrder()
    {
        return orgDeptOrder;
    }

    public void setOrgDeptOrder(String orgDeptOrder)
    {
        this.orgDeptOrder = orgDeptOrder;
    }

    public String getReservedRemark()
    {
        return reservedRemark;
    }

    public void setReservedRemark(String reservedRemark)
    {
        this.reservedRemark = reservedRemark;
    }

    public String getNeedApprove()
    {
        return needApprove;
    }

    public void setNeedApprove(String needApprove)
    {
        this.needApprove = needApprove;
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
     * @return reserved_user_id
     */
    public String getReservedUserId()
    {
        return reservedUserId;
    }

    /**
     * @param reservedUserId
     */
    public void setReservedUserId(String reservedUserId)
    {
        this.reservedUserId = reservedUserId;
    }

    /**
     * @return reserved_user_name
     */
    public String getReservedUserName()
    {
        return reservedUserName;
    }

    /**
     * @param reservedUserName
     */
    public void setReservedUserName(String reservedUserName)
    {
        this.reservedUserName = reservedUserName;
    }

    /**
     * @return reserved_user_dept
     */
    public String getReservedUserDept()
    {
        return reservedUserDept;
    }

    /**
     * @param reservedUserDept
     */
    public void setReservedUserDept(String reservedUserDept)
    {
        this.reservedUserDept = reservedUserDept;
    }

    /**
     * @return reserved_time
     */
    public Date getReservedTime()
    {
        return reservedTime;
    }

    /**
     * @param reservedTime
     */
    public void setReservedTime(Date reservedTime)
    {
        this.reservedTime = reservedTime;
    }

    /**
     * @return order_time_begin
     */
    public Date getOrderTimeBegin()
    {
        return orderTimeBegin;
    }

    /**
     * @param orderTimeBegin
     */
    public void setOrderTimeBegin(Date orderTimeBegin)
    {
        this.orderTimeBegin = orderTimeBegin;
    }

    /**
     * @return order_time_end
     */
    public Date getOrderTimeEnd()
    {
        return orderTimeEnd;
    }

    /**
     * @param orderTimeEnd
     */
    public void setOrderTimeEnd(Date orderTimeEnd)
    {
        this.orderTimeEnd = orderTimeEnd;
    }

    /**
     * @return order_duration
     */
    public Long getOrderDuration()
    {
        return orderDuration;
    }

    /**
     * @param orderDuration
     */
    public void setOrderDuration(Long orderDuration)
    {
        this.orderDuration = orderDuration;
    }

    /**
     * @return meeting_name
     */
    public String getMeetingName()
    {
        return meetingName;
    }

    /**
     * @param meetingName
     */
    public void setMeetingName(String meetingName)
    {
        this.meetingName = meetingName;
    }

    /**
     * 获取1-准备中 2-使用中 3-已结束 4-已取消 0-已删除
     *
     * @return status - 1-准备中 2-使用中 3-已结束 4-已取消 0-已删除
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * 设置1-准备中 2-使用中 3-已结束 4-已取消 0-已删除
     *
     * @param status 1-准备中 2-使用中 3-已结束 4-已取消 0-已删除
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * @return reserved_user_dept_id
     */
    public String getReservedUserDeptId()
    {
        return reservedUserDeptId;
    }

    /**
     * @param reservedUserDeptId
     */
    public void setReservedUserDeptId(String reservedUserDeptId)
    {
        this.reservedUserDeptId = reservedUserDeptId;
    }
}